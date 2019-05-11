defmodule EchoServer do
  require Logger

  def accept(port) do
    {:ok, socket} = :gen_tcp.listen(port,
      [:binary, packet: :line, active: false, reuseaddr: true])
    Logger.info "Accepting connections on port #{port}"
	Logger.info "Enter name of html file"
    loop_acceptor(socket)
  end

  defp loop_acceptor(socket) do
    {:ok, client} = :gen_tcp.accept(socket)
    Task.start_link(fn -> serve(client) end)
    loop_acceptor(socket)
  end

  defp serve(socket) do
    socket |> read_line() |>getFile() |> write_line(socket)
    :ok = :gen_tcp.close(socket)
  end

  defp read_line(socket) do
    {:ok, data} = :gen_tcp.recv(socket, 0)
    data
  end

  defp write_line(line, socket) do
    :gen_tcp.send(socket, line)
  end

  def main(args \\ []) do
    accept(9999)
  end
  
  defp getFile(name) do
	  fileName = "#{name}.html"
	  fileName = String.replace(fileName, "\r\n", "")
	  
	  case File.open(fileName) do
		  {:ok, file} -> "HTTP/1.1 200\nContent-Type: text/plain\nConnection: close\n" <> IO.read(file, :all)
		  {:error, _} -> "HTTP 404\nFile not found"
	  end
  end
  @doc """
  help provided from Myde Rojas,
  https://codewords.recurse.com/issues/five/building-a-web-framework-from-scratch-in-elixir
  https://elixir-lang.org/crash-course.html
  https://elixir-lang.org/getting-started/io-and-the-file-system.html
  https://hexdocs.pm/elixir/File.html
  https://www.tutorialspoint.com/elixir/elixir_file_io.htm
  https://code.tutsplus.com/tutorials/working-with-file-system-in-elixir--cms-28869
  https://joyofelixir.com/8-strings-input-and-output/
  """  
end