package com.data.hemo.mobile.server

import android.content.Context
import android.util.Log
import com.data.hemo.mobile.utils.NetworkUtils
import com.google.android.exoplayer2.C.UTF8_NAME
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
                    
                    <button id="submitBtn" class="btn-submit" type="button">🚀 发送到设备</button>
                    
                    <div id="statusMessage" class="status"></div>
                </div>
                
                <script>
                    // 全局变量
                    var token = "$token";
                    var isSubmitting = false;
                    
                    // DOM 元素引用
                    var textarea = null;
                    var charCount = null;
                    var submitBtn = null;
                    var statusMessage = null;
                    
                    // 页面加载完成后初始化
                    function init() {
                        // 获取DOM元素
                        textarea = document.getElementById('content');
                        charCount = document.getElementById('charCount');
                        submitBtn = document.getElementById('submitBtn');
                        statusMessage = document.getElementById('statusMessage');
                        
                        if (!textarea || !charCount || !submitBtn || !statusMessage) {
                            console.error('Failed to find required DOM elements');
                            return;
                        }
                        
                        // 初始化字符计数
                        updateCharCount();
                        
                        // 绑定事件监听器
                        textarea.addEventListener('input', updateCharCount);
                        submitBtn.addEventListener('click', submitContent);
                        
                        // 添加键盘快捷键
                        textarea.addEventListener('keydown', function(e) {
                            if (e.ctrlKey && e.key === 'Enter') {
                                submitContent();
                            }
                        });
                        
                        console.log('Page initialized successfully');
                    }
                    
                    // 更新字符计数
                    function updateCharCount() {
                        if (textarea && charCount) {
                            var length = textarea.value.length;
                            charCount.textContent = length;
                            
                            // 如果接近限制，显示警告
                            if (length > 4800) {
                                charCount.style.color = '#ff6b6b';
                            } else if (length > 4500) {
                                charCount.style.color = '#ffa726';
                            } else {
                                charCount.style.color = '#999';
                            }
                        }
                    }
                    
                    // 显示状态消息
                    function showStatus(message, type) {
                        if (statusMessage) {
                            statusMessage.textContent = message;
                            statusMessage.className = 'status ' + type;
                            statusMessage.style.display = 'block';
                            
                            // 5秒后自动隐藏
                            setTimeout(function() {
                                statusMessage.style.display = 'none';
                            }, 5000);
                        }
                    }
                    
                    // Base64编码函数
                    function base64Encode(str) {
                        try {
                            // 先转换为UTF-8字节数组
                            var utf8Bytes = [];
                            for (var i = 0; i < str.length; i++) {
                                var charCode = str.charCodeAt(i);
                                if (charCode < 128) {
                                    utf8Bytes.push(charCode);
                                } else if (charCode < 2048) {
                                    utf8Bytes.push(192 | (charCode >> 6));
                                    utf8Bytes.push(128 | (charCode & 63));
                                } else if (charCode < 65536) {
                                    utf8Bytes.push(224 | (charCode >> 12));
                                    utf8Bytes.push(128 | ((charCode >> 6) & 63));
                                    utf8Bytes.push(128 | (charCode & 63));
                                }
                            }
                            
                            // Base64编码
                            var base64Chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
                            var result = '';
                            var i = 0;
                            
                            while (i < utf8Bytes.length) {
                                var byte1 = utf8Bytes[i++] || 0;
                                var byte2 = utf8Bytes[i++] || 0;
                                var byte3 = utf8Bytes[i++] || 0;
                                
                                var triplet = (byte1 << 16) | (byte2 << 8) | byte3;
                                
                                result += base64Chars.charAt((triplet >> 18) & 63);
                                result += base64Chars.charAt((triplet >> 12) & 63);
                                result += base64Chars.charAt((triplet >> 6) & 63);
                                result += base64Chars.charAt(triplet & 63);
                            }
                            
                            // 处理填充
                            var padding = utf8Bytes.length % 3;
                            if (padding === 1) {
                                result = result.substring(0, result.length - 2) + '==';
                            } else if (padding === 2) {
                                result = result.substring(0, result.length - 1) + '=';
                            }
                            
                            return result;
                        } catch (e) {
                            console.error('Base64 encode error:', e);
                            // 如果编码失败，直接返回原始字符串
                            return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, 
                                function(match, p1) {
                                    return String.fromCharCode('0x' + p1);
                                }
                            ));
                        }
                    }
                    
                    // 提交内容
                    function submitContent() {
                        if (isSubmitting) {
                            console.log('Already submitting, please wait');
                            return;
                        }
                        
                        var content = textarea.value.trim();
                        if (!content) {
                            showStatus('请输入内容', 'error');
                            return;
                        }
                        
                        if (content.length > 5000) {
                            showStatus('内容过长，请控制在5000字以内', 'error');
                            return;
                        }
                        
                        // 设置提交状态
                        isSubmitting = true;
                        var originalText = submitBtn.textContent;
                        submitBtn.innerHTML = '<div class="loading"><div class="spinner"></div>发送中...</div>';
                        submitBtn.disabled = true;
                        
                        try {
                            // 编码内容为Base64
                            var encodedContent = base64Encode(content);
                            console.log('Content encoded, length:', encodedContent.length);
                            
                            // 准备请求数据
                            var requestData = {
                                content: encodedContent,
                                timestamp: new Date().toISOString(),
                                encoding: 'base64'
                            };
                            
                            // 发送请求
                            var xhr = new XMLHttpRequest();
                            var url = '/submit?token=' + encodeURIComponent(token);
                            
                            xhr.open('POST', url, true);
                            xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
                            
                            xhr.timeout = 30000; // 30秒超时
                            
                            xhr.onload = function() {
                                isSubmitting = false;
                                submitBtn.textContent = originalText;
                                submitBtn.disabled = false;
                                
                                if (xhr.status === 200) {
                                    try {
                                        var response = JSON.parse(xhr.responseText);
                                        console.log('Response received:', response);
                                        
                                        if (response.success) {
                                            showStatus('✅ 发送成功！设备已收到数据', 'success');
                                            textarea.value = '';
                                            updateCharCount();
                                            
                                            // 3秒后尝试关闭页面
                                            setTimeout(function() {
                                                try {
                                                    if (window.history.length > 1) {
                                                        window.history.back();
                                                    } else {
                                                        window.close();
                                                    }
                                                } catch (e) {
                                                    console.log('Cannot close window:', e);
                                                }
                                            }, 3000);
                                        } else {
                                            showStatus('❌ 发送失败: ' + (response.message || '未知错误'), 'error');
                                        }
                                    } catch (e) {
                                        console.error('JSON parse error:', e);
                                        showStatus('❌ 响应解析失败', 'error');
                                    }
                                } else {
                                    console.error('HTTP error:', xhr.status, xhr.statusText);
                                    showStatus('❌ 服务器错误: HTTP ' + xhr.status, 'error');
                                }
                            };
                            
                            xhr.onerror = function() {
                                isSubmitting = false;
                                submitBtn.textContent = originalText;
                                submitBtn.disabled = false;
                                console.error('Network error');
                                showStatus('❌ 网络错误，请检查连接', 'error');
                            };
                            
                            xhr.ontimeout = function() {
                                isSubmitting = false;
                                submitBtn.textContent = originalText;
                                submitBtn.disabled = false;
                                console.error('Request timeout');
                                showStatus('❌ 请求超时，请重试', 'error');
                            };
                            
                            console.log('Sending request to:', url);
                            xhr.send(JSON.stringify(requestData));
                            
                        } catch (error) {
                            isSubmitting = false;
                            submitBtn.textContent = originalText;
                            submitBtn.disabled = false;
                            console.error('Submit error:', error);
                            showStatus('❌ 提交失败: ' + error.message, 'error');
                        }
                    }
                    
                    // 页面加载完成后初始化
                    if (document.readyState === 'loading') {
                        document.addEventListener('DOMContentLoaded', init);
                    } else {
                        init();
                    }
                </script>
            </body>
            </html>
        """.trimIndent()

        return newFixedLengthResponse(
            Response.Status.OK,
            "text/html; charset=$UTF8_NAME",
            html
        )
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