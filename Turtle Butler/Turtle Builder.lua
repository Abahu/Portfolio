--Turtle Builder
local m = {} --Coordinate array: Y,X,Z where Y is up
local chest = {} --Chest coordinates, first index is X,Y, and Z, second is chest number
local material = 1
local mult = 1
local manifest = {} --Names for each item type 1-9
local pos = {0,0,0}
local start = {0,0,0,0,0,0}
local home={0,0,0}
local dir = 0 --0,1,2,3: NESW
local tm = 0
local id = 0
local usedMats = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
local onlineAddress = ""
local stage = {0,0,0,0}


function initialise(mA)
	mA=mA or {96,96,96};
	if fs.exists("remote/xyz") then
		local file = fs.open("remote/xyz","r")
		local derp = leer(file.readLine())
		file.close()
		if derp[1] then
			pos[1] = tonumber(derp[1])
			pos[2] = tonumber(derp[2])
			pos[3] = tonumber(derp[3])
		else
			stage[1] = 1;
		end
		if derp[4] then
			dir = tonumber(derp[4])
		else
			stage[2] = 1;
		end
		if derp[5] then
			home[1] = tonumber(derp[5])
			home[2] = tonumber(derp[6])
			home[3] = tonumber(derp[7])
		else
			stage[3] = 1;
		end
		if derp[8] then
			onlineAddress = derp[8];
		else
			stage[4] = 1;
		end
	else
		stage = {1,1,1,1};
	end
	for i=1,mA[1] do -- y
		m[i] = {}
		for j=1,mA[2] do -- x
			m[i][j] = {}
			for k=1,mA[3] do -- z
				m[i][j][k] = 0
			end
		end
	end
	for i=1,16 do
		manifest[i] = ""
	end
	for i=1,3 do
		chest[i] = {}
		for j=1,16 do
			chest[i][j] = 0
		end
	end
end

function turn(dir1)
	if dir ~= dir1 then
		if dir - 1 == dir1 or (dir == 0 and dir1 == 3) then
			turtle.turnLeft()
			dir = dir - 1
		elseif dir + 1 == dir1 or (dir == 3 and dir1 == 0) then
			turtle.turnRight()
			dir = dir + 1
		else
			turtle.turnRight()
			turtle.turnRight()
			dir = dir + 2
		end
		if dir < 0 then
			dir = dir + 4
		end
		if dir > 3 then
			dir = dir - 4
		end
	end
end

function posUpdate()
	local file = fs.open("remote/xyz","w")
	file.write(tostring(pos[1]).."~"..tostring(pos[2]).."~"..tostring(pos[3]).."~"..tostring(dir).."~"..tostring(home[1]).."~"..tostring(home[2]).."~"..tostring(home[3]).."~"..onlineAddress.."~;")
	file.close()
end

function moveto(x1,y1,z1) --Y is up
	local l = 0
	turtle.select(nextEmptySlot())
	while pos[1] ~= x1 or pos[2] ~= y1 or pos[3] ~= z1 do
		if turtle.getFuelLevel() == 0 then
			turtle.select(16)
			if turtle.refuel() == false then
				print("Place fuel in slot 16")
				local i = false
				repeat
				i = turtle.refuel()
				until i == true
				posUpdate()
			end
		end
		if l >= 2 then
			if turtle.detectUp() == false then
				turtle.up()
				 pos[2] = pos[2] + 1
				if turtle.getItemCount() then
					turtle.placeDown()
				end
				posUpdate()
			else
				turtle.digUp()
				turtle.up()
				pos[2] = pos[2] + 1
				posUpdate()
			end
			l=0
		end
		
		while pos[1] < x1 and l < 2 do
			turn(1)
			if turtle.detect() == false then
				turtle.forward()
				l = 0
				 pos[1] = pos[1] + 1
				posUpdate()
			else
				l = l + 1
				break
			end
		end
		while pos[1] > x1 and l < 2 do
			turn(3)
			if turtle.detect() == false then
				turtle.forward()
				l = 0
				 pos[1] = pos[1] - 1
				posUpdate()
			else
				l = l + 1
				break
			end
		end
		
		while pos[3] < z1 and l < 2 do
			turn(2)
			if turtle.detect() == false then
				turtle.forward()
				l = 0
				 pos[3] = pos[3] + 1
				posUpdate()
			else
				l = l + 1
				break
			end
		end
		while pos[3] > z1 and l < 2 do
			turn(0)
			if turtle.detect() == false then
				turtle.forward()
				l = 0
				 pos[3] = pos[3] - 1
				posUpdate()
			else
				l = l + 1
				break
			end
		end
		
		if pos[1] == x1 and pos[3] == z1 then
			if pos[2] < y1 then
				if turtle.detectUp() == false then
					turtle.up()
					pos[2] = pos[2] + 1
					if turtle.getItemCount() then
						turtle.placeDown()
					end
					posUpdate()
				else
					turtle.digUp()
					turtle.up()
					pos[2] = pos[2] + 1
					posUpdate()
				end
			elseif pos[2] > y1 then
				if turtle.detectDown() == false then
					turtle.down()
					 pos[2] = pos[2] - 1
					if turtle.getItemCount() then
						turtle.placeUp()
					end
					posUpdate()
				else
					turtle.digDown()
					turtle.down()
					 pos[2] = pos[2] - 1
					posUpdate()
				end
			end
		end
	end
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

function nextEmptySlot()
	for i=1,16 do
		if turtle.getItemCount(i) == 0 then
			return i
		end
	end
end

function nextEmpty(array,emptyVal)
	emptyVal = emptyVal or ""
	local pos = 0
	local isEmpty = false
	while true do
		pos = pos + 1
		if array[pos] then
			if array[pos] == emptyVal then
				isEmpty = true
				break
			end
		else
			break
		end
	end
	return isEmpty, pos
end

function switch(m) -- for CC 1.64+
	local data = turtle.getItemDetail()
	if data.name ~= m then
		for i = 1,16 do
			data = turtle.getItemDetail(i)
			if data.name == m then
				turtle.select(i)
				return true
			elseif data.name ~= m and i == 16 then
				return false
			end
		end
	else
		return true
	end
end

function selectMat(x,y,z)
	material = math.floor(m[y][x][z])
	turtle.select(material)
	while turtle.getItemCount() == 0 do
		resupply(material)
	end
	moveto(x+start[1]-1,y+start[2],z+start[3]-1)
end

function build()
--Block dir: 0 = normal, 1=N,2=E,3=S,4=W
	print("Moving to starting location")
	moveto(start[1], start[2], start[3])
	local flipper = 1 -- 1 or -1; will be 1 when going S and -1 when going N
	local flippand = 0 -- 0 or 1; multiplied by length
	local minDim = {1,1,1}; --y,x,z
	local maxDim = {1,1,1}; --y,x,z
	for i in ipairs(m) do
		for j in ipairs(m[i]) do
			for k in ipairs(m[i]) do
				if m[i][j][k] and m[i][j][k] ~= 0 then
					if minDim[1] > i then
						minDim[1] = i;
					elseif minDim[2] > j then
						minDim[2] = j;
					elseif minDim[3] > k then
						minDim[3] = k;
					end
					
					if maxDim[1] < i then
						maxDim[1] = i;
					elseif maxDim[2] < j then
						maxDim[2] = j;
					elseif maxDim[3] < k then
						maxDim[3] = k;
					end
				end
			end
		end
	end
	--for i=1,64 do --y
	newL = math.abs(maxDim[3]-minDim[3])+2;
	for i=minDim[1],maxDim[1] do
		--for j=1,64 do --x
		for j=minDim[2],maxDim[2] do
			--for k=1,64 do --z
			for k=minDim[3],maxDim[3] do
				if m[i][j][(flipper * k) + (flippand * newL)] and m[i][j][(flipper * k) + (flippand * newL)] ~= 0 then
					print((flipper * k) + (flippand * newL))
					--Checks to see if the object to the east needs to be done first
					if m[i][j+1][(flipper * k) + (flippand * newL)] and 10*( m[i][j+1][(flipper * k) + (flippand * newL)] - math.floor(m[i][j+1][(flipper * k) + (flippand * newL)]) ) == 4 then
						selectMat(j+1,i,(flipper * k) + (flippand * newL))
						moveto(pos[1],pos[2]-1,pos[3])
						turn(1)
						if turtle.detect() then
							turtle.dig()
						else
							turtle.place()
						end
					end
					--Checks to see if the object north or south needs to be done first
					if  m[i][j][(flipper * k) + (flippand * newL) + flipper] and 10*( m[i][j][(flipper * k) + (flippand * newL) + flipper] - math.floor(m[i][j][(flipper * k) + (flippand * newL)] + flipper) ) == 2 - flipper then
						selectMat(j,i,(flipper * k) + (flippand * newL) + flipper)
						moveto(pos[1],pos[2]-1,pos[3])
						turn(1-flipper)
						if turtle.detect() then
							turtle.dig()
						else
							turtle.place()
						end
						
					end
					--Checks to see whether or not this is already placed and, if so, where to place it
					local cV = 10*(m[i][j][(flipper * k) + (flippand * newL)] - math.floor(m[i][j][(flipper * k) + (flippand * newL)]))
					if cV ~= 2-flipper then
						selectMat(j,i,(flipper * k) + (flippand * newL))
						if cV == 0 then
							if turtle.detectDown() then
								turtle.digDown()
								turtle.dropDown()
							end
							turtle.select(material)
							turtle.placeDown()
						elseif cV == 2 then
							moveto(pos[1]+1,pos[2]-1,pos[3])
							turn(3)
							if turtle.detect() then
								turtle.dig()
							else
								turtle.place()
							end
						elseif cV == 2 + flipper then
							moveto(pos[1],pos[2]-1,pos[3]+1)
							turn(1-flipper)
							if turtle.detect() then
								turtle.dig()
							else
								turtle.place()
							end
						end
					end
					
				end
			end
			flipper = flipper * (-1)
			flippand = 1 - flippand
		end
	end
	cleanUp("A")
	moveto(home[1],home[2],home[3])
end


function resupply(item)
	item = item or 0
	print("Resupplying")
	if item ~= 0 then
		moveto(chest[1][item],chest[2][item]+1,chest[3][item])
		turtle.select(item)
		turtle.suckDown(64)
		while turtle.getItemCount() == 0 do
			print("Put "..tostring(manifest[item]).." in slot "..tostring(item))
			os.sleep(5)
		end
	else
		for i=1,16 do
			if usedMats[i] ~= 0 then
				moveto(chest[1][i],chest[2][i]+1,chest[3][i])
				turtle.select(i)
				turtle.suckDown(64)
				while turtle.getItemCount() == 0 do
					print("Put "..tostring(manifest[i]).." in slot "..tostring(i))
					os.sleep(5)
				end
			end
		end
	end
end

function createMap(tableVar)
	local l=0
	repeat
		if tableVar[1+4*l] then
			m[tonumber(tableVar[1+4*l])][tonumber(tableVar[2+4*l])][tonumber(tableVar[3+4*l])] = tonumber(tableVar[4+4*l])
			usedMats[math.floor(tonumber(tableVar[4+4*l]))] = usedMats[math.floor(tonumber(tableVar[4+4*l]))] + 1
		end
		l=l+1
	until not tableVar[1+4*l]
end

function parse(a,message)
	if message == "A" then --Build
		print("Receiving building information")
		id = a
		local a, m1 = rednet.receive() --map
		local b, m2 = rednet.receive() --manifest
		local c, m3 = rednet.receive() --chest location
		local d, m4 = rednet.receive() --start location
		print("Received data")
		print("Processing start location")
		local u = leer(m4)
		start[1] = tonumber(u[1])
		start[2] = tonumber(u[2])
		start[3] = tonumber(u[3])
		print("X: "..u[1].." Y: "..u[2].." Z: "..u[3])
		print("Processing manifest")
		u = leer(m2)
		for i=1,16 do
			manifest[i] = u[i]
		end
		print("Processing chest location")
		u = leer(m3)
		local l = 0
		local i = 1
		repeat
			for i=1,3 do
				chest[i][l+1] = tonumber(u[i+3*l])
			end
			l=l+1
		until not u[i+3*l]
		print("Processing map")
		createMap(leer(m1));
		print("Data processing complete")
		resupply()
		build()
	end
	if message == "B" then --Shut down
		tm = 1
	end
	if message == "C" then --Analyse building
		print("Receiving analysis information")
		id = a
		local a, m1 = rednet.receive() --Coordinates
		print("Received data")
		print("Processing bounds")
		local u = leer(m1)
		for i=1,6 do
			start[i] = tonumber(u[i])
		end
		print("Bounds processed")
		print("Starting analysis")
		analyse()
		local file = fs.open("remote/temp027","r")
		local derp = file.readAll()
		file.close()
		local file = fs.open("remote/temp027","r")
		local doop = file.readAll()
		file.close()
		print("Sending map")
		rednet.send(id,derp)
		os.sleep(0.2)
		print("Sending manifest")
		rednet.send(id,doop)
		print("Going home")
		moveto(home[1],home[2],home[3])
		rednet.send(id,derp)
		os.sleep(0.2)
		rednet.send(id,doop)
		cleanUp()
	end
end

function cleanUp(task)
	task = task or "blank"
	if task == "Depricated" then
		for i=1,16 do
			if chest[1][i] then
				if chest[1][i] ~= 0 then
					moveto(chest[1][i],chest[2][i]+1,chest[3][i])
					turtle.select(i)
					if not turtle.dropDown() then
						turtle.drop()
					end
				end
			end
		end
	end
	for i=1,64 do -- z
		m[i] = {}
		for j=1,64 do -- x
			m[i][j] = {}
			for k=1,64 do -- y
				m[i][j][k] = 0
			end
		end
	end
	for i=1,16 do
		manifest[i] = "minecraft:stone"
	end
	for i=1,3 do
		chest[i] = {}
		for j=1,16 do
			chest[i][j] = 0
		end
	end
	usedMats = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0}
	start = {0,0,0,0,0,0}
	clear()
	print("Awaiting instructions...")
end

function analyse()
	print("Moving to start location:")
	local startR = {} -- Starting value for whichever is the lowest
	for i=1,3 do
		if start[i] <= start[i+3] then
			startR[i] = start[i]
			startR[i+3] = start[i+3]
		else
			startR[i] = start[i+3]
			startR[i+3] = start[i]
		end
	end
	moveto(startR[1],startR[5]+1,startR[3])
	local flipper = 1 --Either 1 or -1. When this is 1, proceed normally. When -1, flippand will be 1 and butler will proceed backwards
	local flippand = 0 --Either 0 or 1. When this is 1, it is multiplied by the length in order to start from the end
	for i=startR[5]-startR[2],0,-1 do --Y is the height
		for j=1,startR[4]-startR[1]+1 do --X
			for k=1,startR[6]-startR[3]+1 do --Z
				local targetX = startR[1] + j - 1
				local targetY = startR[2] + i + 1
				local targetZ = startR[3] + ( flipper * k ) + flippand * ( startR[6] - startR[3] ) - flipper
				moveto(targetX,targetY,targetZ)
				local somethingThere, data = turtle.inspectDown()
				if somethingThere then
					local derp = data.name
					local foop = 0
					local doop = 1
					for l=1,16 do
						if derp == manifest[l] then
							usedMats[l] = usedMats[l] + 1
							m[targetY-startR[2]][targetX-startR[1]+1][targetZ-startR[3]+1] = l
							turtle.select(l)
							doop = 17
							break
						end
						if manifest[l] == "" then
							doop = l
							break
						end
					end
					if doop ~= 17 then
						local empty = {nextEmpty(manifest)}
						if empty[1] == true then
							manifest[empty[2]] = derp
							usedMats[empty[2]] = usedMats[empty[2]] + 1
							m[targetY-startR[2]][targetX-startR[1]+1][targetZ-startR[3]+1] = empty[2]
							turtle.select(empty[2])
						end
					end
					turtle.digDown()
				end
			end
			flipper = flipper * (-1)
			if flippand == 1 then
				flippand = 0
			else
				flippand = 1
			end
		end
	end
	save()
end

function save()
	local file = fs.open("remote/temp027.bdf", "w")
	for i=1,64 do
		for j=1,64 do
			for k=1,64 do
				if m[i][j][k] ~= 0 then
					file.write(tostring(i).."~"..tostring(j).."~"..tostring(k).."~"..tostring(m[i][j][k]).."~")
				end
			end
		end
	end
	file.write(";")
	file.close()
	file = fs.open("remote/temp027Manifest.bmf", "w")
	for i=1,16 do
		file.write(tostring(i).."~"..manifest[i].."~"..tostring(usedMats[i]).."~")
	end
	file.write(";")
	file.close()
	change = 0
end

function clear()
	term.clear();
	term.setCursorPos(1,1);
end

function get(onlineAddress)
    local response = http.get(
        "http://pastebin.com/raw.php?i="..textutils.urlEncode( onlineAddress )
    )
       
    if response then
        local sResponse = response.readAll()
        response.close()
        return sResponse
    end
end

---------------------------
---------------------------


if peripheral.isPresent("right") and peripheral.getType("right") == "modem" then
	rednet.open("right")
	side = "right"
elseif peripheral.isPresent("left") and peripheral.getType("left") == "modem" then
	rednet.open("left")
	side = "left"
else
	print("Please attach a wireless modem")
	os.sleep(5)
	clear()
	tm = 1
end

initialise()

if stage[1] == 1 then
	clear()
	print("Current x coordinate")
	local i = read()
	if i then pos[1] = tonumber(i) end
	print("Current y coordinate")
	i = read()
	if i then pos[2] = tonumber(i) end
	print("Current z coordinate")
	i = read()
	if i then pos[3] = tonumber(i) end
end
if stage[2] == 1 then
	clear()
	print("Current direction: N:0, E:1, S:2, W:3")
	local i = read()
	if i then dir = tonumber(i) end
end
if stage[3] == 1 then
	clear()
	print("Home x coordinate")
	local i = read()
	if i then home[1] = tonumber(i) end
	print("Home y coordinate")
	i = read()
	if i then home[2] = tonumber(i) end
	print("Home z coordinate")
	i = read()
	if i then home[3] = tonumber(i) end
	fs.makeDir("remote/")
	posUpdate()
end
if stage[4] == 1 then
	clear()
	print("Pastebin code for remote control")
	local i = read()
	if i then onlineAddress = i end
end

if pos[1] == home[1] and pos[2] == home[2] and pos[3] == home[3] then
else
	print("Go home? y/n")
	local i = read()
	if i then
		if i == "y" then 
			moveto(home[1],home[2],home[3]) 
		end
	end
end
posUpdate();
clear()
print("Awaiting instructions...")

while tm ~= 1 do
	if tm == 1 then break end
	if tm == 0 then -- Main
		local event, a, b, c = os.pullEvent()
		if event == "rednet_message" then
			print("Message received.")
			parse(a,b)
		end
		if event == "key" then
			if a == 207 then
				tm = 1
			end
		end
	end
	if tm == 2 then
		local response = get(onlineAddress);
		if response then
			local file = io.tmpfile();
			file:write(response);
			for line in pairs(file:lines()) do
				if line == "build" then
					createMap(leer(foo));
					local u = leer(file:lines());
					start[1] = u[1];
					start[2] = u[2];
					start[3] = u[3];
				elseif line == "start settlement" then
					startSettlement(leer(file:lines()), tonumber(file:lines()), leer(file:lines()), leer(file:lines()));
				elseif line == "mine" then
					local positions = leer(file:lines());
					for i = 1,3 do
						p1 = positions[i];
						p2 = positions[i+3];
						if p2 > p1 then
							positions[i] = p2;
							positions[i+3] = p1;
						end
					end
					for i = positions[1],positions[4] do
						
					end
				elseif line == "connect" then
					
				elseif line == "start literal" then
					local weeb;
					while weeb ~= "end literal" do
						weeb = file:lines();
						if weeb ~= "end literal" then
							shell.run(weeb);
						end
					end
				end
			end
		end
	end
end
rednet.close(side)