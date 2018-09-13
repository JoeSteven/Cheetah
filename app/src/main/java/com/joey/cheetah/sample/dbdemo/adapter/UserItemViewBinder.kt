package com.joey.cheetah.sample.dbdemo.adapter

import android.view.View
import com.joey.cheetah.core.list.AbsItemViewBinder
import com.joey.cheetah.core.list.AbsViewHolder
import com.joey.cheetah.sample.R
import com.joey.cheetah.sample.dbdemo.db.User
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Description:
 * author:Joey
 * date:2018/9/13
 */
class UserItemViewBinder : AbsItemViewBinder<User, UserItemViewBinder.UserViewHolder>() {

    override fun layout(): Int {
        return R.layout.item_user
    }

    override fun createViewHolder(itemView: View): UserViewHolder {
        return UserViewHolder(itemView)
    }

    override fun onBind(holder: UserViewHolder, item: User) {
        holder.itemView.tvName.text = "Name:${item.name}"
        holder.itemView.tvAge.text = "Age:${item.age}"
    }



    inner class UserViewHolder(override val containerView: View?) : AbsViewHolder<User>(containerView),
            LayoutContainer

}