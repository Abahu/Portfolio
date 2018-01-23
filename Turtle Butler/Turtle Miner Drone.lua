dir = 0
id = 0
u={"",""}

function turn(dirN)
	local dir1 = dir + dirN
	if dir1 < 0 then
		dir1 = dir1 + 4
	end
	if dir1 > 3 then
		dir1 = dir1 - 4
	end
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
	rednet.message(id,"Turn:"..tostring(dirN)..":done:;")
end

function forward()
	rednet.message(id,"Forward:"..tostring(turtle.forward())..":"..tostring(turtle.detect())..":done:;")
end

function up()
	rednet.message(id,"Up:"..tostring(turtle.up())..":"..tostring(turtle.detectUp())..":done:;")
end

function down()
	rednet.message(id,"Down:"..tostring(turtle.down())..":"..tostring(turtle.detectDown())..":done:;")	
end

function dig()
	turtle.dig()
	rednet.message(id,"Dig:"..tostring(turtle.detect())..":done:;")
end

function digUp()
	turtle.digUp()
	rednet.message(id,"digUp:"..tostring(turtle.detect())..":"done:;")
end

function digDown()
	turtle.digDown()
	rednet.message(id,"digDown"..tostring(turtle.detect())..":"done:;")
end

function scan()
	rednet.message(id,"Scan:"..tostring(turtle.detect())..":done:;")
end

function fullScan()
	for i=1,4 do
	scan()
	turn(dir+i)
	end
end

function leer(m)
	l=1
	mode=mode or 0
	local i = 1
	local p = ""
	repeat
		repeat
			if m:sub(l,l) ~= ":" then
				p=p..m:sub(l,l)
				l=l+1
			end
		until m:sub(l,l) == ":"
		u[i] = p
		p=""
		l=l+1
		i=i+1
	until m:sub(l,l) == ";"
end

function parse(m)
	leer(m)
	if u[1] == "Forward" then forward() end
	if u[1] == "Up" then up() end
	if u[1] == "Down" then down() end
	if u[1] == "Dig" then dig() end
	if u[1] == "digUp" then digUp() end
	if u[1] == "digDown" then digDown() end
	if u[1] == "Turn" then turn(tonumber(u[2])) end
	if u[1] == "Scan" then scan() end
	if u[1] == "fullScan" then fullScan() end
	u[1], u[2] = "",""
end

while true do
	local e={os.pullEvent()}
	if e[1] == "rednet_message" then 
		parse(e[3])
		id = e[2]
	end
	if e[1] == "key" and e[2] == key.q then break end
end