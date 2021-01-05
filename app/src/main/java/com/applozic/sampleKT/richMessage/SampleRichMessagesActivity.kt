package com.applozic.sampleKT.richMessage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference
import com.applozic.mobicomkit.api.conversation.Message
import com.applozic.mobicomkit.api.conversation.MessageBuilder
import com.applozic.mobicomkit.exception.ApplozicException
import com.applozic.mobicomkit.listners.MediaUploadProgressHandler
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity
import com.applozic.sampleKT.R
import java.util.*


class SampleRichMessagesActivity : AppCompatActivity() {
    var receiverUserId: String? = null

    enum class RichMessage(val value: String) {
        // Rich message payload
        LinkButtonPayload(Payload.link),
        SuggestedReplyPayload(Payload.suggestedReply),
        SubmitButtonPayload(Payload.submit),
        ImagePayload(Payload.image),
        ListPayload(Payload.list),
        GenericCardPayload(Payload.genericCard),
        CardCarouselPayload(Payload.cardCarousel);

        internal object Payload {
            const val link =
                "[{ \"type\": \"link\", \"url\": \"https://www.google.com\", \"name\": \"Go To Google\" },{ \"type\": \"link\", \"url\": \"https://www.facebook.com\", \"name\": \"Go To Facebook\" }]"
            const val suggestedReply =
                "[{ \"title\": \"Yes\", \"message\": \"Cool! send me more.\" },{ \"title\": \"No\", \"message\": \"Not at all\"}]"
            const val image =
                "[{ \"caption\": \"Image caption\", \"url\": \"https://images.pexels.com/photos/544980/pexels-photo-544980.jpeg?cs=srgb&dl=dew-drop-droplet-544980.jpg&fm=jpg\"}]"
            const val submit =
                "[{ \"name\": \"Pay\", \"replyText\":\"optional, will be used as acknowledgement message to user in case of requestType JSON. Default value is same as name parameter\" }]"
            const val list =
                "{ \"headerImgSrc\": \"https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940\", \"headerText\": \"Header text.\", \"elements\": [{ \"imgSrc\": \"https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940\", \"title\": \"List item 1\", \"description\": \"Description for the list item\", \"action\": { \"url\": \"https://www.google.com\", \"type\": \"link\" } }], \"buttons\": [{ \"name\": \"See us on facebook\", \"action\": { \"url\": \"https://www.facebook.com\", \"type\": \"link\" } }]}"
            const val genericCard =
                "[ { \"title\": \"Card Title\", \"subtitle\": \"Card Subtitle \", \"header\": { \"overlayText\": \"Overlay Text\", \"imgSrc\": \"https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940\" }, \"description\": \"This is a sample description of the card. It is for sampling purposes.\", \"titleExt\": \"Title extension\", \"buttons\": [ { \"name\": \"Open facebook\", \"action\": { \"type\": \"link\", \"payload\": { \"url\": \"https://www.facebook.com\" } } } ] } ]"
            const val cardCarousel =
                "[ { \"title\": \"OYO Rooms 1\", \"subtitle\": \"Kundanahalli road turn.\", \"header\": { \"overlayText\": \"$400\", \"imgSrc\": \"http://www.tollesonhotels.com/wp-content/uploads/2017/03/hotel-room.jpg\" }, \"description\": \"Bharathi Road \\n Near Head Post Office\", \"titleExt\": \"4.2/5\", \"buttons\": [ { \"name\": \"Link Button\", \"action\": { \"type\": \"link\", \"payload\": { \"url\": \"https://www.facebook.com\" } } }, { \"name\": \"Suggested Reply\", \"action\": { \"type\": \"quickReply\", \"payload\": { \"message\": \"text will be sent as message\", \"replyMetadata\": { \"key1\": \"value1\" } } } }, { \"name\": \"Submit button\", \"action\": { \"type\": \"submit\", \"payload\": { \"text\": \"acknowledgement text\", \"formData\": { \"amount\": \"$55\", \"description\": \"movie ticket\" }, \"formAction\": \"https://reqres.in/api/users\", \"requestType\": \"json\" } } } ] }, { \"title\": \"OYO Rooms 2\", \"subtitle\": \"Kundanahalli \", \"header\": { \"overlayText\": \"$360\", \"imgSrc\": \"http://www.tollesonhotels.com/wp-content/uploads/2017/03/hotel-room.jpg\" }, \"description\": \"Bharathi Road | Near Head Post Office, Cuddalore 607001\", \"titleExt\": \"4.2/5\", \"buttons\": [ { \"name\": \"Link Button\", \"action\": { \"type\": \"link\", \"payload\": { \"url\": \"https://www.facebook.com\" } } }, { \"name\": \"Submit button\", \"action\": { \"type\": \"submit\", \"payload\": { \"text\": \"acknowledgement text\", \"formData\": { \"amount\": \"$22\", \"description\": \"movie ticket\" }, \"formAction\": \"https://example.com/book\", \"requestType\": \"json\" } } }, { \"name\": \"Suggested Reply\", \"action\": { \"type\": \"quickReply\", \"payload\": { \"message\": \"text will be sent as message\", \"replyMetadata\": { \"key1\": \"value1\" } } } } ] }, { \"title\": \"OYO Rooms 3\", \"subtitle\": \"Kundanahalli \", \"header\": { \"overlayText\": \"$750\", \"imgSrc\": \"http://www.tollesonhotels.com/wp-content/uploads/2017/03/hotel-room.jpg\" }, \"description\": \"Bharathi Road | Near Head Post Office, Cuddalore 607001\", \"titleExt\": \"4.2/5\", \"buttons\": [ { \"name\": \"Link Button\", \"action\": { \"type\": \"link\", \"payload\": { \"url\": \"https://www.facebook.com\" } } }, { \"name\": \"Submit button\", \"action\": { \"type\": \"submit\", \"payload\": { \"text\": \"acknowledgement text\", \"formData\": { \"amount\": \"$45\", \"description\": \"movie ticket\" }, \"formAction\": \"https://example.com/book\", \"requestType\": \"json\" } } }, { \"name\": \"Suggested Reply\", \"action\": { \"type\": \"quickReply\", \"payload\": { \"message\": \"text will be sent as message\", \"replyMetadata\": { \"key1\": \"value1\" } } } } ] } ]"
        }

        // Template id of rich message types
        class Template {
            object Button {
                const val linkOrSubmit = "3"
                const val suggestedReply = "6"
            }

            object Image {
                const val type = "9"
            }

            object List {
                const val type = "7"
            }

            object Card {
                const val type = "10"
            }
        }

        // Message text for rich messages
        class Message {
            object Button {
                const val link = "Here are the links you can checkout"
                const val submit = "This sample message for pay click"
                const val suggestedReply = "Do you want some more updates?"
            }

            internal object Image {
                const val text = "This is below sample image"
            }

            internal object List {
                const val text = "This is a sample list."
            }

            internal object Card {
                const val genericText = "This is a sample generic Card message."
                const val cardCarouselText = "This is a sample Card Carousel message."
            }
        }

        companion object {
            // ContentType of rich message
            const val contentType = "300"

            // Will be used in submit button
            const val requestType = "json"
            const val formData = "{\"amount\": \"1000\",\"description\": \"movie ticket\"}"
            const val formDataActionUrl = "https://reqres.in/api/users"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rich_messages)
        val linkButton = findViewById<Button>(R.id.linkButton)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val suggestedButton = findViewById<Button>(R.id.suggestedReplies)
        val imageRichMessageButton = findViewById<Button>(R.id.imageRichMessageButton)
        val listRichMessageButton = findViewById<Button>(R.id.listRichMessageButton)
        val genericCardRichMessageButton = findViewById<Button>(R.id.genericCardRichMessageButton)
        val cardCarouselRichMessageButton = findViewById<Button>(R.id.cardCarouselRichMessageButton)

        // Passed the login userId for testing in two way
        // Note: receiverUserId needs to be different userId not the login userId
        receiverUserId = MobiComUserPreference.getInstance(this).userId

        linkButton.setOnClickListener(View.OnClickListener {
            sendRichMessageAndLaunchConversation(
                receiverUserId,
                RichMessage.Message.Button.link,
                RichMessage.Template.Button.linkOrSubmit,
                RichMessage.LinkButtonPayload.value
            )
        })
        suggestedButton.setOnClickListener(View.OnClickListener {
            sendRichMessageAndLaunchConversation(
                receiverUserId,
                RichMessage.Message.Button.suggestedReply,
                RichMessage.Template.Button.suggestedReply,
                RichMessage.SuggestedReplyPayload.value
            )
        })
        submitButton.setOnClickListener(View.OnClickListener {
            val messageMetadata: MutableMap<String, String> = HashMap()
            messageMetadata["contentType"] = RichMessage.Companion.contentType
            messageMetadata["templateId"] = RichMessage.Template.Button.linkOrSubmit
            messageMetadata["payload"] = RichMessage.SubmitButtonPayload.value
            messageMetadata["formData"] = RichMessage.Companion.formData
            messageMetadata["formAction"] = RichMessage.Companion.formDataActionUrl
            messageMetadata["requestType"] = RichMessage.Companion.requestType
            sendRichMessageAndLaunchConversation(
                receiverUserId,
                messageMetadata,
                RichMessage.Message.Button.submit
            )
        })
        imageRichMessageButton.setOnClickListener(View.OnClickListener {
            sendRichMessageAndLaunchConversation(
                receiverUserId,
                RichMessage.Message.Button.suggestedReply,
                RichMessage.Template.Image.type,
                RichMessage.ImagePayload.value
            )
        })
        listRichMessageButton.setOnClickListener(View.OnClickListener {
            sendRichMessageAndLaunchConversation(
                receiverUserId,
                RichMessage.Message.List.text,
                RichMessage.Template.List.type,
                RichMessage.ListPayload.value
            )
        })
        genericCardRichMessageButton.setOnClickListener(View.OnClickListener {
            sendRichMessageAndLaunchConversation(
                receiverUserId,
                RichMessage.Message.Card.genericText,
                RichMessage.Template.Card.type,
                RichMessage.GenericCardPayload.value
            )
        })
        cardCarouselRichMessageButton.setOnClickListener(View.OnClickListener {
            sendRichMessageAndLaunchConversation(
                receiverUserId,
                RichMessage.Message.Card.cardCarouselText,
                RichMessage.Template.Card.type,
                RichMessage.CardCarouselPayload.value
            )
        })
    }

    private fun sendRichMessageAndLaunchConversation(
        receiverUserId: String?,
        message: String?,
        templateId: String,
        payload: String
    ) {
        val messageMetadata: MutableMap<String, String> = HashMap()
        messageMetadata["contentType"] = RichMessage.Companion.contentType
        messageMetadata["templateId"] = templateId
        messageMetadata["payload"] = payload
        val messageBuilder = MessageBuilder(this)
            .setMessage(message)
            .setTo(receiverUserId)
            .setMetadata(messageMetadata)
        sendMessageAndLaunchChat(messageBuilder)
    }

    private fun sendRichMessageAndLaunchConversation(
        receiverUserId: String?,
        messageMetadata: Map<String, String>?,
        message: String?
    ) {
        val messageBuilder = MessageBuilder(this)
            .setMessage(message)
            .setTo(receiverUserId)
            .setMetadata(messageMetadata)
        sendMessageAndLaunchChat(messageBuilder)
    }

    private fun sendMessageAndLaunchChat(messageBuilder: MessageBuilder) {
        messageBuilder.send(object : MediaUploadProgressHandler {
            override fun onUploadStarted(e: ApplozicException?, oldMessageKey: String?) {
            }

            override fun onProgressUpdate(
                percentage: Int,
                e: ApplozicException?,
                oldMessageKey: String?
            ) {
            }

            override fun onCancelled(e: ApplozicException?, oldMessageKey: String?) {
            }

            override fun onCompleted(e: ApplozicException?, oldMessageKey: String?) {
            }

            override fun onSent(message: Message?, oldMessageKey: String?) {
                launchConversation(messageBuilder.messageObject.to)
            }
        })
    }

    private fun launchConversation(receiverUserId: String?) {
        val takeOrderIntent = Intent(this, ConversationActivity::class.java)
        takeOrderIntent.putExtra(ConversationUIService.TAKE_ORDER, true)
        takeOrderIntent.putExtra(ConversationUIService.USER_ID, receiverUserId)
        startActivity(takeOrderIntent)
    }
}