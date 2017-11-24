package com.gs.amqp

import com.gs.amqp.TestMessage.TestThis

import collection.JavaConverters._
import java.net.URLClassLoader
import java.net.URL

import org.specs2.Specification

import org.springframework.amqp.core.{Message, MessageProperties}
import org.springframework.amqp.AmqpRejectAndDontRequeueException

class ProtobufMessageConverterSpec extends Specification { def is = s2"""
  The ProtobufConverter
    should convert a protocol buffer into an AMQP message                                   ${sut().pb2AMQP}
    should convert an AMQP message into a protocol buffer                                   ${sut().amqp2PB}
    should throw an AmqpRejectAndDontRequeueException if there's no message_type_name field ${sut().noType}
"""
  case class sut() {
    val converter = new ProtobufMessageConverter(TestMessage.getDescriptor())
    val pbMsg = TestThis.newBuilder.setMsg("Hello World!").build

    def pb2AMQP = {
      val amqpMsg = converter.createMessage(pbMsg, new MessageProperties)
      val headers = amqpMsg.getMessageProperties.getHeaders
      (amqpMsg.getBody must be_==(pbMsg.toByteArray)) and
        (headers.asScala must haveKey(ProtobufMessageConverter.MESSAGE_TYPE_NAME)) and
        (headers.get(ProtobufMessageConverter.MESSAGE_TYPE_NAME) must be_==("TestThis"))
    }

    def amqp2PB = {
      val props = {
        val p = new MessageProperties
        p.setHeader(ProtobufMessageConverter.MESSAGE_TYPE_NAME, pbMsg.getDescriptorForType.getName)
        p
      }

      val amqpMsg = new Message(pbMsg.toByteArray, props)
      val msg = converter.fromMessage(amqpMsg)
      msg must be_==(pbMsg)
    }

    def noType = {
      val amqpMsg = new Message(pbMsg.toByteArray, new MessageProperties)
      converter.fromMessage(amqpMsg) must throwA[AmqpRejectAndDontRequeueException]
    }
  }
}
