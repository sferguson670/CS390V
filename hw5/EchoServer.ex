defmodule EchoServer do
  require Logger

  def accept(port) do
    {:ok, socket} = :gen_tcp.listen(port,
      [:binary, packet: :line, active: false, reuseaddr: true])
    Logger.info "Accepting connections on port #{port}"
	IO.puts "Enter name of html file"
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
	  file = "#{name}.html"
	  
	  case File.open(file) do
		  {:ok, file} -> "HTTP/1.1 200\nContent-Type: text/plain\nConnection: close\n" <> File.read(file)
		  {:error, _} -> "File not found\nHTTP 404\n"
	  end
  end
  @doc """
  help provided from Myde Rojas,
  https://codewords.recurse.com/issues/five/building-a-web-framework-from-scratch-in-elixir
  https://elixir-lang.org/crash-course.html
  https://elixir-lang.org/getting-started/io-and-the-file-system.html
  """  
end