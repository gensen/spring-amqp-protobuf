package com.gs.amqp

import com.google.protobuf.{Message => PB, DynamicMessage}
import com.google.protobuf.Descriptors.FileDescriptor

import org.springframework.amqp.core.{Message, MessageProperties}
import org.springframework.amqp.support.converter._

class ProtobufMessageConverter(descriptor: FileDescriptor) extends AbstractMessageConverter {
  def fromMessage(msg: Message) = try {
    val parsedMessage = for {
      name <- getMessageTypeName(msg)
      messageType <- Option(descriptor.findMessageTypeByName(name))
    } yield DynamicMessage.parseFrom(messageType, msg.getBody)
    if (parsedMessage.isEmpty) {
      throw new MessageConversionException("Unknown message type %s".format(getMessageTypeName(msg)))
    }
    parsedMessage.orNull
  } catch {
    case e => throw new MessageConversionException("Unable to parse the message", e)
  }

  private def getMessageTypeName(msg: Message): Option[String] = {
    val headers = msg.getMessageProperties.getHeaders
    Option(headers.get(ProtobufMessageConverter.MESSAGE_TYPE_NAME)).map(_.toString)
  }

  def createMessage(obj: Any, props: MessageProperties): Message = {
    if (!obj.isInstanceOf[PB]) {
      throw new MessageConversionException("Message wasn't a protobuf")
    } else {
      val protobuf = obj.asInstanceOf[PB]
      val byteArray = protobuf.toByteArray
      props.setContentLength(byteArray.length)
      props.setContentType(ProtobufMessageConverter.CONTENT_TYPE_PROTOBUF)
      props.setHeader(ProtobufMessageConverter.MESSAGE_TYPE_NAME,
                      protobuf.getDescriptorForType.getName)
      new Message(byteArray, props)
    }
  }
}

object ProtobufMessageConverter {
  val MESSAGE_TYPE_NAME = "message_type_name"
  val CONTENT_TYPE_PROTOBUF = "application/x-google-protobuf"
}
