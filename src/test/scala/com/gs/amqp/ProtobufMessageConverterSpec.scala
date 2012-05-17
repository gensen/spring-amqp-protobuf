package com.gs.amqp

import com.gs.amqp.TestMessage.TestThis

import java.net.URLClassLoader
import java.net.URL

import org.scalatest.FunSpec
import org.scalatest.matchers.MustMatchers

import org.springframework.amqp.core.{Message, MessageProperties}

class ProtobufMessageConverterSpec extends FunSpec with MustMatchers {
  describe("The ProtobufConverter") {
    val converter = new ProtobufMessageConverter(TestThis.getDefaultInstance)
    val pbMsg = TestThis.newBuilder.setMsg("Hello World!").build
    it("should convert a protocol buffer into an AMQP message") {
      val amqpMsg = converter.createMessage(pbMsg, new MessageProperties)
      val headers = amqpMsg.getMessageProperties.getHeaders
      amqpMsg.getBody must equal (pbMsg.toByteArray)
    }
    it("should convert an AMQP message into a protocol buffer") {
      val props = new MessageProperties
      val amqpMsg = new Message(pbMsg.toByteArray, props)
      val msg = converter.fromMessage(amqpMsg)
      msg must be(pbMsg)
    }
  }
}
