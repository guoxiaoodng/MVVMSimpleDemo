package com.gk.world.comprehensive.server

import android.content.Context
import android.util.Log
import com.gk.world.comprehensive.utils.NetworkUtils
import fi.iki.elonen.NanoHTTPD
import org.json.JSONObject
import java.util.*

class LocalHttpServer(
    private val context: Context,
    port: Int = 8080
) : NanoHTTPD("0.0.0.0", port) {

    private var dataReceiver: ((String) -> Unit)? = null
    private val sessions = mutableMapOf<String, SessionData>()
    private val tokenTimeout = 5 * 60 * 1000L // 5分钟超时

    data class SessionData(
        val token: String,
        val createdAt: Long = System.currentTimeMillis(),
        var data: String? = null
    )

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        val params = session.parameters
        val method = session.method

        Log.d("LocalHttpServer", "Request: $uri, Method: $method")

        return try {
            when {
                uri == "/" -> serveHomePage(session)
                uri.startsWith("/edit") -> serveEditPage(session)
                uri.startsWith("/submit") -> handleSubmit(session)
                uri.startsWith("/status") -> handleStatusCheck(session)
                else -> newFixedLengthResponse(
                    Response.Status.NOT_FOUND,
                    MIME_PLAINTEXT,
                    "404 Not Found"
                )
            }
        } catch (e: Exception) {
            newFixedLengthResponse(
                Response.Status.INTERNAL_ERROR,
                MIME_PLAINTEXT,
                "Error: ${e.message}"
            )
        }
    }

    private fun serveHomePage(session: IHTTPSession): Response {
        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>QR Data Transfer</title>
                <style>
                    body { font-family: Arial, sans-serif; padding: 20px; text-align: center; }
                    h1 { color: #333; }
                    p { color: #666; margin: 20px 0; }
                    .info { background: #f0f8ff; padding: 15px; border-radius: 5px; margin: 20px 0; }
                </style>
            </head>
            <body>
                <h1>📱 QR Data Transfer</h1>
                <div class="info">
                    <p>请扫描设备上生成的二维码来传输数据</p>
                    <p>服务器运行中...</p>
                </div>
            </body>
            </html>
        """.trimIndent()

        return newFixedLengthResponse(Response.Status.OK, "text/html", html)
    }

    private fun serveEditPage(session: IHTTPSession): Response {
        val token = session.parameters["token"]?.firstOrNull() ?: return createErrorResponse("Missing token")

        // 清理过期会话
        cleanupExpiredSessions()

        // 创建新会话
        if (!sessions.containsKey(token)) {
            sessions[token] = SessionData(token)
        }

        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>输入内容</title>
                <style>
                    * { box-sizing: border-box; }
                    body { 
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                        margin: 0;
                        padding: 20px;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                    }
                    .container {
                        max-width: 500px;
                        margin: 0 auto;
                        background: white;
                        border-radius: 15px;
                        padding: 30px;
                        box-shadow: 0 20px 60px rgba(0,0,0,0.3);
                    }
                    h1 { 
                        color: #333;
                        text-align: center;
                        margin-bottom: 30px;
                        font-weight: 600;
                    }
                    .form-group {
                        margin-bottom: 20px;
                    }
                    label {
                        display: block;
                        margin-bottom: 8px;
                        color: #555;
                        font-weight: 500;
                    }
                    textarea {
                        width: 100%;
                        height: 150px;
                        padding: 12px;
                        border: 2px solid #e0e0e0;
                        border-radius: 8px;
                        font-size: 16px;
                        font-family: inherit;
                        resize: vertical;
                        transition: border-color 0.3s;
                    }
                    textarea:focus {
                        outline: none;
                        border-color: #667eea;
                    }
                    .char-count {
                        text-align: right;
                        color: #999;
                        font-size: 14px;
                        margin-top: 5px;
                    }
                    .btn-submit {
                        width: 100%;
                        padding: 15px;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        border: none;
                        border-radius: 8px;
                        font-size: 16px;
                        font-weight: 600;
                        cursor: pointer;
                        transition: transform 0.2s, box-shadow 0.2s;
                    }
                    .btn-submit:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
                    }
                    .btn-submit:active {
                        transform: translateY(0);
                    }
                    .status {
                        margin-top: 20px;
                        padding: 15px;
                        border-radius: 8px;
                        text-align: center;
                        display: none;
                    }
                    .success {
                        background: #d4edda;
                        color: #155724;
                        border: 1px solid #c3e6cb;
                    }
                    .error {
                        background: #f8d7da;
                        color: #721c24;
                        border: 1px solid #f5c6cb;
                    }
                    .loading {
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        gap: 10px;
                    }
                    .spinner {
                        width: 20px;
                        height: 20px;
                        border: 3px solid #f3f3f3;
                        border-top: 3px solid #667eea;
                        border-radius: 50%;
                        animation: spin 1s linear infinite;
                    }
                    @keyframes spin {
                        0% { transform: rotate(0deg); }
                        100% { transform: rotate(360deg); }
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>📝 输入要传输的内容</h1>
                    <div class="form-group">
                        <label for="content">内容：</label>
                        <textarea 
                            id="content" 
                            placeholder="请输入文本内容，最多5000字..."
                            maxlength="5000"></textarea>
                        <div class="char-count">
                            <span id="charCount">0</span>/5000
                        </div>
                    </div>
                    
                    <button class="btn-submit" onclick="submitContent()">🚀 发送到设备</button>
                    
                    <div id="statusMessage" class="status"></div>
                </div>
                
                <script>
                    const token = "$token";
                    let isSubmitting = false;
                    
                    // 字符计数
                    const textarea = document.getElementById('content');
                    const charCount = document.getElementById('charCount');
                    
                    textarea.addEventListener('input', function() {
                        charCount.textContent = this.value.length;
                    });
                    
                    async function submitContent() {
                        if (isSubmitting) return;
                        
                        const content = textarea.value.trim();
                        if (!content) {
                            showStatus('请输入内容', 'error');
                            return;
                        }
                        
                        if (content.length > 5000) {
                            showStatus('内容过长，请控制在5000字以内', 'error');
                            return;
                        }
                        
                        isSubmitting = true;
                        const submitBtn = document.querySelector('.btn-submit');
                        const originalText = submitBtn.textContent;
                        submitBtn.innerHTML = '<div class="loading"><div class="spinner"></div>发送中...</div>';
                        submitBtn.disabled = true;
                        
                        try {
                            const response = await fetch('/submit?token=' + token, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                },
                                body: JSON.stringify({
                                    content: content,
                                    timestamp: new Date().toISOString()
                                })
                            });
                            
                            const result = await response.json();
                            
                            if (result.success) {
                                showStatus('✅ 发送成功！设备已收到数据', 'success');
                                textarea.value = '';
                                charCount.textContent = '0';
                                
                                // 3秒后自动关闭页面（如果是微信浏览器，无法真正关闭）
                                setTimeout(() => {
                                    try {
                                        window.close();
                                    } catch (e) {
                                        // 忽略关闭错误
                                    }
                                }, 3000);
                            } else {
                                showStatus('❌ 发送失败: ' + (result.error || '未知错误'), 'error');
                            }
                        } catch (error) {
                            showStatus('❌ 网络错误: ' + error.message, 'error');
                        } finally {
                            isSubmitting = false;
                            submitBtn.textContent = originalText;
                            submitBtn.disabled = false;
                        }
                    }
                    
                    function showStatus(message, type) {
                        const statusEl = document.getElementById('statusMessage');
                        statusEl.textContent = message;
                        statusEl.className = 'status ' + type;
                        statusEl.style.display = 'block';
                        
                        // 5秒后自动隐藏
                        setTimeout(() => {
                            statusEl.style.display = 'none';
                        }, 5000);
                    }
                    
                    // 回车键提交（Ctrl+Enter）
                    textarea.addEventListener('keydown', function(e) {
                        if (e.ctrlKey && e.key === 'Enter') {
                            submitContent();
                        }
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        return newFixedLengthResponse(Response.Status.OK, "text/html", html)
    }

    private fun handleSubmit(session: IHTTPSession): Response {
        if (session.method != Method.POST) {
            return newFixedLengthResponse(
                Response.Status.METHOD_NOT_ALLOWED,
                MIME_PLAINTEXT,
                "Method not allowed"
            )
        }

        val token = session.parameters["token"]?.firstOrNull() ?: return createErrorResponse("Missing token")
        val sessionData =
            sessions[token] ?: return createJsonResponse(false, "Session expired or invalid")

        try {
            val files = mutableMapOf<String, String>()
            session.parseBody(files)
            val postData = files["postData"] ?: return createJsonResponse(false, "No data received")

            // 解析JSON数据
            val json = JSONObject(postData)
            val content = json.optString("content", "")

            if (content.isBlank()) {
                return createJsonResponse(false, "Content is empty")
            }

            // 保存数据
            sessionData.data = content

            // 通知接收者
            dataReceiver?.invoke(content)

            // 记录日志
            Log.d("LocalHttpServer", "Received data for token $token: ${content.take(50)}...")

            return createJsonResponse(true, "Data received successfully")

        } catch (e: Exception) {
            Log.e("LocalHttpServer", "Error handling submit", e)
            return createJsonResponse(false, "Server error: ${e.message}")
        }
    }

    private fun handleStatusCheck(session: IHTTPSession): Response {
        val token = session.parameters["token"]?.firstOrNull() ?: return createErrorResponse("Missing token")
        val sessionData = sessions[token]

        return if (sessionData?.data != null) {
            createJsonResponse(true, "Data available", mapOf("data" to sessionData.data))
        } else {
            createJsonResponse(false, "No data yet")
        }
    }

    private fun cleanupExpiredSessions() {
        val now = System.currentTimeMillis()
        val expiredTokens = sessions.filterValues { now - it.createdAt > tokenTimeout }.keys
        expiredTokens.forEach { sessions.remove(it) }
    }

    private fun createJsonResponse(success: Boolean, message: String, data: Map<String, Any?> = emptyMap()): Response {
        val response = mutableMapOf<String, Any?>(
            "success" to success,
            "message" to message
        )
        response.putAll(data)

        val json = JSONObject(response).toString()
        return newFixedLengthResponse(Response.Status.OK, "application/json", json)
    }

    private fun createErrorResponse(message: String): Response {
        return createJsonResponse(false, message)
    }

    fun setDataReceiver(receiver: (String) -> Unit) {
        this.dataReceiver = receiver
    }

    fun generateToken(): String {
        return UUID.randomUUID().toString().substring(0, 8)
    }

    fun getServerUrl(token: String): String {
        val ip = NetworkUtils.getLocalIpAddress(context) ?: "127.0.0.1"
        return "http://$ip:$listeningPort/edit?token=$token"
    }

    fun checkData(token: String): String? {
        return sessions[token]?.data
    }

    fun clearSession(token: String) {
        sessions.remove(token)
    }

    override fun stop() {
        super.stop()
        sessions.clear()
        dataReceiver = null
    }
}