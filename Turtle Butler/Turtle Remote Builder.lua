m = {} --Coordinate arrays: [level][x][y] 1-9 (material types)
am = {} --Array for coordinates of options
mo = {6,2,0,0,2,2,0}
chest = {}
manifest = {} --Names for material types
usedMats = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
matChar = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"}
start = {0,0,0}
dir = 0

cx=1 --Cursor position
cy=1
px=1 --Mouse cursor click position
py=11
cursorChar = {"x","^",">","v","<"};
lvl=1
material = 1

--Control button mechanism
ctrl = 0;
cPos = {{0,0},{0,0},{0,0}}; --Positions for the control; y1,y2; x1,x2; z1,z2;

menu=0
option=1
shut = 0
tm=0

change = 0
name = ""
db = 0

id = 0
side = ""

bound = 96
function ia() --Initialise our arrays
	if not fs.exists("remote/") then fs.makeDir("remote/") end
	for i=1,bound do
		m[i] = {}
		for j=1, bound do
			m[i][j] = {}
			for k=1, bound do
				m[i][j][k] = 0;
			end
		end
	end
	for i=1,4 do
		am[i] = {}
		for j=1,50 do
			am[i][j] = {}
			for k=1,19 do
				am[i][j][k] = 0
			end
		end
	end
	for i=1,16 do
		manifest[i] = "minecraft:stone"
	end
	for i=1,3 do
		chest[i] = {}
		for j=1,9 do
			chest[i][j] = 0
		end
	end
end

function du()
	term.clear()
	term.setCursorPos(1,1)
	term.write("==================================================")
	term.setCursorPos(1,3)
	if menu == 8 then
			term.setCursorPos(1,18)
	end
	term.write("==================================================")
	for xv = 1, 50 do
		for yv = 4, 18 do
			term.setCursorPos(xv,yv)
			if cx+xv-25 >= 1 and cy+yv-9 >= 1 and cx+xv-25 <= bound and cy+yv-9 <= bound then
				local cm = m[lvl][cx+xv-25][cy+yv-9]
				if cm ~= 0 then
					term.write(matChar[math.floor(cm)])
				else
					term.write(" ")
				end
			else
				term.write("!")
			end
		end
	end
	if ctrl == 1 then
		term.setCursorPos(50,19);
		term.write("#");
	end
	term.setCursorPos(px-cx+25,py-cy+9);
	term.write(cursorChar[dir+1]);
	term.setCursorPos(1,19)
	if db == 1 then
		term.write("File doesn't exist")
	end
	if menu == 0 then
		term.setCursorPos(1,2)
		term.write("| Layer: "..tostring(lvl))
		term.setCursorPos(14,2)
		term.write("material: "..matChar[material])
		term.setCursorPos(27,2)
		term.write("x: "..tostring(px))
		term.setCursorPos(34,2)
		term.write("z: "..tostring(py))
		term.setCursorPos(41,2)
		term.write("dir: "..tostring(dir))
		term.setCursorPos(50,2)
		term.write("|")
	elseif menu == 1 then
		term.setCursorPos(1,2)
		term.write("|    Load   Save   Send   Help   Quit   Exit     |")
		term.setCursorPos(-2+7*option,2)
		term.write(">")
	elseif menu == 2 or menu == 5 or menu == 6 then
		if menu == 2 then
			term.setCursorPos(23,1)
			term.write("Quit?")
		elseif menu == 5 then
			term.setCursorPos(23,1)
			term.write("Save")
		elseif menu == 6 then
			term.setCursorPos(22,1)
			term.write("Cancel?")
		end
		term.setCursorPos(1,2)
		term.write("|          No                       Yes          |")
		if option == 1 then
			term.setCursorPos(11,2)
		else
			term.setCursorPos(36,2)
		end
		term.write(">")
	elseif menu == 3 then
		term.setCursorPos(1,2)
		term.write("| File Name:                                     |")
		term.setCursorPos(14,2)
		term.setCursorBlink(true)
		load(read())
		term.setCursorBlink(false)
		menu=0
		option=1
		du()
	elseif menu == 4 then
		term.setCursorPos(1,2)
		term.write("| Project Name:                                  |")
		term.setCursorPos(17,2)
		term.setCursorBlink(true)
		name = read()
		if name ~= "" then
			save()
			if shut == 1 then
				tm=1
			end
			menu = 0
		else
			menu = 6
		end
		term.setCursorBlink(false)
		du()
		option=1
		menu=0
	elseif menu == 7 then
		term.setCursorPos(1,2)
		term.write("| Turtle ID:                                     |")
		term.setCursorPos(14,2)
		term.setCursorBlink(true)
		local tid = read()
		if tid == "$r" then 
			send(true)
			menu=0
			option=1
			du()
		else
			if tonumber(tid) then
				id = tonumber(tid)
				local derp = ""
				for i=1,16 do --Get Chest locations
					if usedMats[i] ~= 0 then
						for j=1,3 do
							if j==1 then
								derp = "X"
							elseif j==2 then
								derp = "Y"
							else
								derp = "Z"
							end
							term.setCursorPos(1,2)
							term.write("| Chest "..tostring(i).." "..derp..":                                     |")
							term.setCursorPos(14,2)
							derp = read() or "0"
							if derp then
								chest[j][i] = tonumber(derp)
							else
								chest[j][i] = 0
							end
						end
					end
				end
				for i=1,3 do
					if i==1 then
						derp = "X"
					elseif i==2 then
						derp = "Y"
					else
						derp = "Z"
					end
					while not tonumber(derp) do
						term.setCursorPos(1,2)
						term.write("| Start Coordinate "..derp..":                                     |")
						term.setCursorPos(23,2)
						derp = read()
					end
					start[i] = tonumber(derp)
				end
				term.setCursorPos(1,19)
				send()
			end
			menu = 0
			option = 1
			du()
		end
	end
end
function leer(m,l,mode)
	l=l or 1
	mode=mode or 0
	local i = 1
	local p = ""
	local r = {}
	local u = {}
	repeat
		repeat
			if m:sub(l,l) ~= "~" then
				p=p..m:sub(l,l)
				l=l+1
			end
		until m:sub(l,l) == "~"
		if mode == 0 then
			u[i] = tonumber(p)
		else
			r[i] = p
		end
		p=""
		l=l+1
		i=i+1
	until m:sub(l,l) == ";"
	if mode == 0 then
		return u
	else
		return r
	end
end

function save()
local file = fs.open("remote/"..name..".bdf", "w")
for i=1,bound do
	for xv=1,bound do
		for yv=1,bound do
			if m[i][xv][yv] ~= 0 then
				local cm = m[i][xv][yv]
				file.write(tostring(i).."~"..tostring(xv).."~"..tostring(yv).."~"..tostring(cm).."~")
				usedMats[math.floor(cm)] = usedMats[math.floor(cm)] + 1
			end
		end
	end
end
file.write(";")
file.close()
file = fs.open("remote/"..name..".bmf", "w")
for i=1,16 do
	file.write(tostring(i).."~"..manifest[i].."~"..tostring(usedMats[i]).."~")
end
file.write(";")
file.close()
change = 0
end

function send(resend)
	term.setCursorPos(1,19)
	if resend then
		rednet.send(id,"A")
		os.sleep(0.2)
		local file = fs.open("remote/038temp.bdf", "r")
		local message = file.readAll()
		file.close()
		local file = fs.open("remote/038debugger.bmf","r")
		id = tonumber(file.readLine())
		rednet.send(id,message)
		os.sleep(0.2)
		for i=1,3 do
			rednet.send(id,file.readLine())
			os.sleep(0.2)
		end
		file.close()
		term.write("done")
	else
		rednet.send(id,"A")
		term.write("2:")
		os.sleep(0.2)
		local tname = name --Send the map
		local tchange = change
		name = "038temp"
		save()
		local file = fs.open("remote/038temp.bdf","r")
		rednet.send(id,file.readAll())
		file.close()
		name = tname
		change = tchange
		term.write("3:")
		os.sleep(0.2)
	   
		local file = fs.open("remote/038debugger.bmf", "w")
		file.writeLine(tostring(id))
		local message = "" --Send the manifest
		for i=1,16 do
			message = message..manifest[i].."~"
		end
		message = message..";"
		file.writeLine(message)
		rednet.send(id, message)
		term.write("4:")
		os.sleep(0.2)
	   
		message = "" --Send the chest coordinates
		for i=1,16 do
			if chest[2][i] ~= 0 then
				message = message..tostring(chest[1][i]).."~"..tostring(chest[2][i]).."~"..tostring(chest[3][i]).."~"
			end
		end
		message = message..";"
		rednet.send(id, message)
		file.writeLine(message)
		term.write("5:")
		os.sleep(0.2)
	   
		message = "" --Send the start coordinates
		message = start[1].."~"..start[2].."~"..start[3].."~"..";"
		rednet.send(id,message)
		file.writeLine(message)
		file.close()
		--term.write(message)
		os.sleep(0.2)
	end
end

function load(f)
	if f ~= "" then
		if fs.exists("remote/" .. f .. ".bdf") then
			ia()
			local file = fs.open("remote/" .. f .. ".bdf", "r")
			local data = file.readAll()
			file.close()
			if data ~= "" and data ~= ";" then
				local u = leer(data)
				local j=0
				repeat
					if u[1+4*j] then
						m[u[1+4*j]][u[2+4*j]][u[3+4*j]]=u[4+4*j]
					end
					j=j+1
				until not u[1+4*j]
			end
			j=0
			file = fs.open("remote/"..f..".bmf", "r")
			data = file.readAll()
			if data ~= "" and data ~= ";" then
				local r = leer(data,1,1)
				while r[1+3*j] do
					manifest[tonumber(r[1+3*j])] = r[2+3*j]
					usedMats[tonumber(r[1+3*j])] = tonumber(r[3+3*j])
					j=j+1
				end
			end
			file.close();
		else
			db = 1
		end
		name = f
	end
end
function kr()
	if mo[menu] >= option + 1 then
		option = option + 1
		du()
	end
end
function kl()
	if option ~= 1 then
		option = option-1
		du()
	end
end
function enter()
	if menu == 1 then
		if option == 1 then
			menu=3
		elseif option == 2 then
			option=1
			if name == "" then
				menu=4
			else
				save()
			end
		elseif option == 3 then
			option=1
			menu=7
		elseif option == 4 then
			option=1
			menu=8
		elseif option == 5 then
			option=1
			menu=2
		elseif option == 6 then
			option=1
			menu=0
		end
	elseif menu == 2 then
		if option == 1 then
			option=1
			menu=1
		else
			if change == 1 then
				option=1
				menu=5
			else
				option = 1
				tm = 1
				menu = 0
			end
		end
	elseif menu == 5 then
		if option == 2 then
			if name == "" then
				menu=4
				shut=1
			else
				save()
				menu=0
				option=1
				tm=1
			end
		else
			option=1
			tm=1
			menu=0
		end
	elseif menu == 6 then
		if option == 1 then
			menu = 4
		else
			option = 1
			menu = 0
		end
	end
end

function mapPress(x,y,z,del) --del = 0 for not delete
	del = del or 0
	del = 1 - del;
	if x >= 1 and x <= bound and z >= 1 and z <= bound then
		if ctrl == 0 then
			m[y][x][z] = (material + (dir/10))*del;
			change = 1;
		else
			if cPos[1][1] == 0 then
				cPos[1][1], cPos[2][1], cPos[3][1] = y, x, z;
			else
				cPos[1][2], cPos[2][2], cPos[3][2] = y, x, z;
				for i = math.min(cPos[1][1],cPos[1][2]),math.max(cPos[1][1],cPos[1][2]) do --y
					for j = math.min(cPos[2][1],cPos[2][2]),math.max(cPos[2][1],cPos[2][2]) do --x
						for k = math.min(cPos[3][1],cPos[3][2]),math.max(cPos[3][1],cPos[3][2]) do --z
							m[i][j][k] = (material + (dir/10))*del;
						end
					end
				end
				cPos = {{0,0},{0,0},{0,0}};
				change = 1;
			end
		end
	end
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
term.setCursorBlink(false)
ia()
du()
while tm ~= 1 do
	local event, a, b, c = os.pullEvent()
	if event == "key" then
		if a == 1 and menu ~= 0 then
			option = 1
			menu = 0
		end
		if a == 200 and menu == 0 then
			if cy ~= 1 then
				cy = cy-1
				py=cy
			end
		end
		if a == 208 and menu == 0 then
			if cy ~= bound then
				cy =cy+1
				py=cy
			end
		end
		if a == 205 then
			if menu ~= 0 then
				kr()
			elseif cx ~= bound then
				cx=cx+1
				px=cx
			end
		end
		if a == 203 then
			if menu ~= 0 then
				kl()
			elseif cx ~= 1 then
				cx=cx-1
				px=cx
			end
		end
		if a == 14 and menu == 0 then
			mapPress(cx,lvl,cy,1);
		end
		if a == 24 and menu == 0 then
			cx, cy = 1, 1;
			px, py = 1, 1;
		end
		if a == 28 then
			if menu == 0 then
				menu = 1
			else
				enter()
			end
		end
		if a == 57 and menu == 0 then
			mapPress(cx,lvl,cy);
		end
		if a == 12 and lvl ~= 1 and menu == 0 then
			lvl=lvl-1
		end
		if a == 207 and menu == 0 then
			tm = 1
		end
		if a == 13 and lvl ~= bound and menu == 0 then
			lvl=lvl+1
		end
		if a == 2  and menu == 0  then
			material=material-1;
			if material <= 0 then material = 16-material end
		end
		if a == 3 and menu == 0  then
			material=(material) % 16 + 1;
		end
		if a == 74 then
			dir = dir - 1
			if dir < 0 then dir = 5 + dir end
		end
		if a == 78 then
			dir = (dir + 1) % 5;
		end
		if a == 29 then
			ctrl = 1 - ctrl;
			cPos = {{0,0},{0,0},{0,0}};
		end
		du()
	end
	if event == "mouse_click" then
		if menu ~= 0 then
			--if am[menu][a][b] ~= 0 then
			--	option = am[menu][a][b]
			--	enter()
			--end
		else
			local xv = cx-25+b;
			local yv = cy-9+c;
			px=xv;
			py=yv;
			mapPress(xv,lvl,yv,a-1);
			du();
		end
	end
	if event == "mouse_drag" then
		if menu == 0 then
			local xv = cx-25+b;
			local yv = cy-9+c;
			px=xv;
			py=yv;
			mapPress(xv,lvl,yv,a-1);
			du();
		end
	end
	if menu == 99 then
		tm = 1
	end
end
rednet.close(side)
term.setCursorPos(1,1)
term.clear()