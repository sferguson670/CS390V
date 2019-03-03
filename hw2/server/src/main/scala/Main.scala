import java.net._
import java.io._
import scala.io._

object EchoServer {
  def read_and_write(in: BufferedReader, out:BufferedWriter): Unit = {
    println("Enter html name file")
    val request = in.readLine()
    val file = new File("./" + request + ".html")
    if (!file.exists()) {
      println("File not found")
      out.write("HTTP 404")
    }
    else {
      println("HTTP/1.1 200") //version & status code
      println("Content-Type: text/plain") //type of data
      println("Connection: close") //will close stream
      println("\r\n") //end of headers

      out.write(scala.io.Source.fromFile(request + ".html").mkString)
    }
    out.flush()
    in.close()
    out.close()
  }

  def serve(server: ServerSocket): Unit = {
    val s = server.accept()
    val in = new BufferedReader(new InputStreamReader(s.getInputStream))
    val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))

    read_and_write(in, out)

    s.close()
  }

  def main(args: Array[String]) {
    val server = new ServerSocket(9999)
    println("Listening for connection on port 9999 ...")
    while(true) {
      serve(server)
    }
  }
}

//Used Oracle's Java "All about sockets" documentation
