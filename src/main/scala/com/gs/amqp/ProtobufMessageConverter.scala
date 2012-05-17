package com.gs.amqp

import com.google.protobuf.{Message => PB}

import org.springframework.amqp.core.{Message, MessageProperties}
import org.springframework.amqp.support.converter._

class ProtobufMessageConverter[T <: PB](defaultInstance: T) extends AbstractMessageConverter {
  def fromMessage(msg: Message) = try {
    defaultInstance.newBuilderForType.mergeFrom(msg.getBody).build
  } catch {
    case e => throw new MessageConversionException("Unable to parse the message", e)
  }

  def createMessage(obj: Any, props: MessageProperties): Message = {
    if (!obj.isInstanceOf[PB]) {
      throw new MessageConversionException("Message wasn't a protobuf")
    } else {
      val protobuf = obj.asInstanceOf[PB]
      val byteArray = protobuf.toByteArray
      props.setContentLength(byteArray.length)
      props.setContentType(MessageProperties.CONTENT_TYPE_BYTES)
      new Message(byteArray, props)
    }
  }
}
