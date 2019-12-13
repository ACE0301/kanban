package com.ace.homework2.model.actions.mapper

import android.content.Context
import com.ace.homework2.R
import com.ace.homework2.model.actions.data.Action
import com.ace.homework2.model.actions.data.ActionsPresModel

interface ActionDataToActionsPresModel {

    fun map(context: Context, from: List<Action>): List<ActionsPresModel>
}

class ActionDataToActionsPresModelImpl : ActionDataToActionsPresModel {
    override fun map(context: Context, from: List<Action>): List<ActionsPresModel> =
        from.filterNot { action ->
            action.type == "updateCard" && action.data.card.desc.isNullOrEmpty()
        }
            .map { action ->
                ActionsPresModel(
                    memberCreator = action.memberCreator,
                    data = action.data,
                    id = action.id,
                    type = action.type,
                    date = action.date,
                    previewUrl = action.data.attachment?.previewUrl.orEmpty(),
                    actionInfo = when (
                        action.type
                        ) {
                        "addMemberToCard" -> context.getString(
                            R.string.add_member_to_card_text,
                            action.memberCreator.fullName,
                            action.member.fullName
                        )
                        "createCard" -> context.getString(
                            R.string.create_card_text,
                            action.memberCreator.fullName,
                            action.data.list.name
                        )
                        "addAttachmentToCard" -> {
                            context.getString(
                                R.string.add_attachment_to_card_text,
                                action.memberCreator.fullName,
                                action.data.attachment?.name
                            )
                        }
                        "updateCard" -> {
                            when (action.data.old.desc) {
                                "" -> context.getString(
                                    R.string.update_card_add_description_text,
                                    action.memberCreator.fullName,
                                    action.data.card.desc
                                )
                                else -> context.getString(
                                    R.string.update_card_change_description_text,
                                    action.memberCreator.fullName,
                                    action.data.old.desc,
                                    action.data.card.desc
                                )
                            }
                        }
                        else -> ""
                    }
                )
            }
}
