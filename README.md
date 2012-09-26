# Spring AMQP Protocol Buffer Converter

Message converter to and from Protocol Buffers for
[Spring AMQP](http://www.springsource.org/spring-amqp).  Usage is
pretty easy, you'll just need to configure your RabbitTemplate to use
a converter created for that particular Protocol Buffer descriptor:

    val template = new RabbitTemplate
    template.setMessageConverter(
        new ProtobufMessageConverter(Protobuf.getDescriptor)
    )
    template.convertAndSend(myProtobuf)
    val anotherProtobuf = template.receive()

## License
Copyright (c) 2012 Aaron D. Valade
Licensed under the MIT license.
