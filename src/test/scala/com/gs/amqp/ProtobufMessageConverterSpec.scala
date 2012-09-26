package com.gs.amqp

import com.gs.amqp.TestMessage.TestThis

import java.net.URLClassLoader
import java.net.URL

import org.scalatest.FunSpec
import org.scalatest.matchers.MustMatchers

import org.springframework.amqp.core.{Message, MessageProperties}

class ProtobufMessageConverterSpec extends FunSpec with MustMatchers {
  describe("The ProtobufConverter") {
    val converter = new ProtobufMessageConverter(TestMessage.getDescriptor())
    val pbMsg = TestThis.newBuilder.setMsg("Hello World!").build

    it("should convert a protocol buffer into an AMQP message") {
      val amqpMsg = converter.createMessage(pbMsg, new MessageProperties)
      val headers = amqpMsg.getMessageProperties.getHeaders
      amqpMsg.getBody must equal (pbMsg.toByteArray)
      headers must contain key (ProtobufMessageConverter.MESSAGE_TYPE_NAME)
      headers.get(ProtobufMessageConverter.MESSAGE_TYPE_NAME) must be ("TestThis")
    }

    it("should convert an AMQP message into a protocol buffer") {
      val props = {
        val p = new MessageProperties
        p.setHeader(ProtobufMessageConverter.MESSAGE_TYPE_NAME, pbMsg.getDescriptorForType.getName)
        p
      }

      val amqpMsg = new Message(pbMsg.toByteArray, props)
      val msg = converter.fromMessage(amqpMsg)
      msg must be(pbMsg)
    }
  }
}
