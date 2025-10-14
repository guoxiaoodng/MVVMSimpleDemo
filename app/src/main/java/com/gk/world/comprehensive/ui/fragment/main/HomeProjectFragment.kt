package com.gk.world.comprehensive.ui.fragment.main

import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.drake.brv.utils.*
import com.drake.engine.base.EngineNavFragment
import com.drake.net.Post
import com.drake.net.utils.scope
import com.drake.net.utils.scopeNetLife
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.bean.AllProjectBean
import com.gk.world.comprehensive.bean.ProjectTypeBeanItem
import com.gk.world.comprehensive.databinding.FragmentHomeProjectBinding
import com.gk.world.net.constance.http.HttpPortConst
import com.gk.world.net.converter.GsonNestConverter

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/13
 *@time: 10:49
 *@description： 首页项目列表
 */

class HomeProjectFragment :
    EngineNavFragment<FragmentHomeProjectBinding>(R.layout.fragment_home_project) {
    override fun initData() {
        getBeans()
    }

    override fun initView() {
        /*try {
            binding.rv.linear().setup {
                addType<AllProjectBean>(R.layout.fragment_main_home_my_project)
            }
            scopeNetLife {
                binding.rvIc.linear(orientation = LinearLayout.HORIZONTAL).divider {
                    setDivider(8, true)
                }.setup {
                    var lastCheckPos = 0
                    var checkPos = 0
                    addType<ProjectTypeBeanItem>(R.layout.fragment_app_ic)
                    onBind {
                        val name = findView<AppCompatTextView>(R.id.name)
                        if (modelPosition == checkPos) {
                            name.setBackgroundResource(R.drawable.shape_b_theme_c_4)
                            name.setTextColor(resources.getColor(R.color.white))
                        } else {
                            name.setBackgroundResource(R.drawable.shape_b_null_c_4_s_eee)
                            name.setTextColor(resources.getColor(R.color.gray_999))
                        }
                    }
                    R.id.item.onClick {
                        val pt = getModel<ProjectTypeBeanItem>()
                        lastCheckPos = checkPos
                        checkPos = modelPosition
                        notifyItemChanged(checkPos)
                        notifyItemChanged(lastCheckPos)
                        getBeans(pt.id)
                    }
                }
                val data =
                    Post<List<ProjectTypeBeanItem>>(HttpPortConst.Java.MY_PROJECT_TYPE) {
                        json("typeId" to 4)
                    }.await()
                val list = data.toMutableList()
                list.add(0, ProjectTypeBeanItem("", "全部项目"))
                binding.rvIc.models = list
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

    private fun getBeans(s: String = "") {
        /*binding.page.onRefresh {
            scope {
                addData(Post<List<AllProjectBean>>(HttpPortConst.Java.MY_PROJECT + "?rows=10&page=" + index) {
                    converter = GsonNestConverter("records")
                    json("attractInvestmentSpeed" to s,
                        "createId" to "",
                        "enterpriseType" to "",
                        "params" to "")
                }.await(), binding.rv.bindingAdapter)
            }.finally {
                if (it is Exception) {
                    binding.rv.models = null
                    loaded = false
                    showError()
                }
            }
        }.autoRefresh()*/
    }

}