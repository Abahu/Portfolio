--Hack-E end; parallel threading to allow for incognito hacking
function getHack()
	local file = fs.open("repos/id","r");
	local id = tonumber(file.readLine());
	file.close();
	local mom = true;
	while mom == true do
		local r = {rednet.receive()};
		if r[1] == id then
			if r[2] == "sd" then
				mom = false;
			else
				assert(loadstring(r[2]))();
				local x,y = term.getCursorPos();
				if x ~= 3 then term.setTextColour(fgShell); term.setCursorPos(1,y); term.write("> "); term.setTextColour(fgText); end
			end
		end
	end
end

function leer(m,l)
	l=l or 1;
	local i = 1;
	local p = "";
	local u = {};
	repeat
		repeat
			if m:sub(l,l) ~= " " then
				p=p..m:sub(l,l);
				l=l+1;
			end
		until m:sub(l,l) == " "
		u[i] = p;
		p="";
		l=l+1;
		i=i+1;
	until m:sub(l,l) == ";"
	return u
end

function normalShell()
	local mom = true;
	if term.isColour() then
		fgError,fgShell, fgText = colours.red, colours.yellow, colours.white;
	else
		fgError, fgShell, fgText = colours.white, colours.white, colours.white;
	end
	term.setTextColour(fgShell); term.clear(); term.setCursorPos(1,1); term.write("CraftOS 1.7"); term.setCursorPos(1,2);
	while mom == true do
		term.setTextColour(fgShell); term.write("> "); term.setTextColour(fgText); local derp = read();
		if derp == "$dd" then
			mom = false; print("Bye bye!");
		else
			derp = derp.." ;"; local u = leer(derp);
			if fs.exists(u[1]) then
				shell.run(unpack(u));
			elseif fs.exists("rom/programs/"..u[1]) then
				if u[1] == "copy" or u[1] == "edit" or u[1] == "delete" or u[1] == "move" or u[1] == "lua" then term.setTextColour(fgError); print("Access denied");
				else
					u[1] = "rom/programs/"..u[1]; shell.run(unpack(u));
				end
			else
				term.setTextColour(fgError); print("No such program");
			end
		end
	end
end

parallel.waitForAny(getHack,normalShell);