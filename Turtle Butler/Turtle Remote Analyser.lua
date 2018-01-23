start = {0,0,0,0,0,0}

shut = 0
tm=0

id = {0}
side = ""
name = {""}

function ia() --Initialisation
	if not fs.exists("remote/") then fs.makeDir("remote/") end
end

function leer(m,l)
	l=l or 1
	local i = 1
	local p = ""
	local u = {}
	repeat
		repeat
			if m:sub(l,l) ~= "~" then
				p=p..m:sub(l,l)
				l=l+1
			end
		until m:sub(l,l) == "~"
		u[i] = p
		p=""
		l=l+1
		i=i+1
	until m:sub(l,l) == ";"
	return u
end

function nextEmpty(arrayVar)
	local i=1
	local open = false
	repeat
		i=i+1
		if arrayVar[i] then
			if arrayVar[i] == 0 then
				open = true
				break
			end
		end
	until not arrayVar[i]
	return open,i
end

function getArrayPos(var,arrayVar)
	local i=0
	local exists = false
	repeat
		i=i+1
		if arrayVar[i] then
			if arrayVar[i] == var then
				exists = true
				break
			end
		end
	until not arrayVar[i]
	return exists,i
end

if peripheral.isPresent("right") and peripheral.getType("right") == "modem" then
	rednet.open("right")
	side = "right"
elseif peripheral.isPresent("back") and peripheral.getType("back") == "modem" then
	rednet.open("back")
	side = "back"
elseif peripheral.isPresent("top") and peripheral.getType("top") == "modem" then
	rednet.open("top")
	side = "top"
elseif peripheral.isPresent("left") and peripheral.getType("left") == "modem" then
	rednet.open("left")
	side = "left"
else
  print("Please attach a wireless modem")
  os.sleep(5)
  term.clear()
  term.setCursorPos(1,1)
  tm = 1
end

term.clear()
term.setCursorPos(1,1)
term.setCursorBlink(false)
ia()

print("Press enter to commence")
while tm ~= 1 do
	local event, a, b, c = os.pullEvent()
	if event == "key" then
		if a == 28 then --Enter was pressed
			local open, pos = nextEmpty(id)
			local derp = ""
			local resend = false
			while not tonumber(derp) do
				print("Enter Turtle ID (A number)")
				derp = read()
				if derp == "$r" then
					resend = true
					break
				end
			end
			
			if resend then
				local file = fs.open("remote/last","r")
				id[pos] = tonumber(file.readLine())
				name[pos] = file.readLine()
				local derp = file.readLine()
				file.close()
				
				print("Sending Turtle ID: "..tostring(id[pos]).." off")
				rednet.send(id[pos],"C")
				os.sleep(0.2)
				rednet.send(id[pos],derp)
			else
				id[pos] = tonumber(derp)
				print("Enter new file name")
				name[pos] = read()
				local chars = {"X","Y","Z","X","Y","Z"}
				local state = {"Start ","Start ","Start ","End " ,"End ","End "}
				for i=1,6 do
					derp = ""
					while not tonumber(derp) do
						print(state[i]..chars[i].." coordinate")
						derp = read()
					end
					start[i] = tonumber(derp)
				end
				print("Sending Turtle ID: "..tostring(id[pos]).." off")
				rednet.send(id[pos],"C")
				os.sleep(0.2)
				derp = ""
				for i=1,6 do
					derp = derp..tostring(start[i]).."~"
				end
				derp = derp..";"
				local file = fs.open("remote/last","w")
				file.writeLine(tostring(id[pos]))
				file.writeLine(name[pos])
				file.writeLine(derp)
				file.close()
				rednet.send(id[pos],derp)
			end
		end
		if a == 207 then --Terminate
			local i = 0
			local exists = false
			repeat
				i=i+1
				if id[i] then
					if id[i] ~= 0 then
						exists = true
					end
				end
			until not id[i]
			if exists then
				print("Some Turtles are still analysing")
				print("Continue termination? y/n")
				if read() == "y" then
					tm = 1
					print("Terminating")
				else
					print("Termination cancelled")
				end
			end
		end
	end
	if event == "rednet_message" and getArrayPos(a,id) then
		print("Received a message")
		local map = b
		print("Received manifest")
		local a, mani = rednet.receive()
		local dunce, pos = getArrayPos(a,id)
		local file = fs.open("remote/test", "w")
		file.write(map)
		file.close()
		file = fs.open("remote/testManifest", "w")
		file.write(mani)
		file.close()
	end
end
term.clear()
term.setCursorPos(1,1)
rednet.close(side)