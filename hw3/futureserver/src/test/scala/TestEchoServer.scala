import java.io.{BufferedReader, BufferedWriter, ByteArrayInputStream, ByteArrayOutputStream}
import java.net._

import org.scalatest._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class TestEchoServer extends FlatSpec with Matchers with MockitoSugar {
  //tests html file that can't be found
  "Bytes in/bytes out" should "with invalid file" in {
    val serversocket = mock[ServerSocket]
    val socket = mock[Socket]
    val bytearrayinputstream = new ByteArrayInputStream("wow".getBytes())
    val bytearrayoutputstream = new ByteArrayOutputStream()

    when(serversocket.accept()).thenReturn(socket)
    when(socket.getInputStream).thenReturn(bytearrayinputstream)
    when(socket.getOutputStream).thenReturn(bytearrayoutputstream)

    EchoServer.serve(serversocket)

    bytearrayoutputstream.toString() should be("HTTP 404")
    verify(socket).close()
  }

  //tests html file that can be found
  "Bytes in/bytes out" should "with valid file" in {
    val serversocket = mock[ServerSocket]
    val socket = mock[Socket]
    val bytearrayinputstream = new ByteArrayInputStream("index".getBytes())
    val bytearrayoutputstream = new ByteArrayOutputStream()

    when(serversocket.accept()).thenReturn(socket)
    when(socket.getInputStream).thenReturn(bytearrayinputstream)
    when(socket.getOutputStream).thenReturn(bytearrayoutputstream)

    EchoServer.serve(serversocket)

    bytearrayoutputstream.toString() should be("<head>\n<title>Index</title>\n</head>\n<body>\n<hl>This is the homepage</hl>\n</body>\n</html>\n")
    verify(socket).close()

    //tests html file that can't be found
    "Read and write" should "with invalid file" in {
      val in = mock[BufferedReader]
      val out = mock[BufferedWriter]

      when(in.readLine()).thenReturn("wow")

      EchoServer.read_and_write(in, out)

      verify(out).write("HTTP 404")
      verify(out).flush()
      verify(out).close()
      verify(in).close()
    }

    //tests html file that can be found
    "Read and write" should "with valid file" in {
      val in = mock[BufferedReader]
      val out = mock[BufferedWriter]

      when(in.readLine()).thenReturn("index")

      EchoServer.read_and_write(in, out)

      verify(out).write("<head>\n<title>Index</title>\n</head>\n<body>\n<hl>This is the homepage</hl>\n</body>\n</html>\n")
      verify(out).flush()
      verify(out).close()
      verify(in).close()
    }
  }
}