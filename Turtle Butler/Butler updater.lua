if not http then
    printError( "Updater requires http API" )
    printError( "Set http_enable to true in ComputerCraft.cfg" )
    return
end
 
local function get(code)
    local response = http.get(
        "http://pastebin.com/raw.php?i="..textutils.urlEncode( code )
    )
       
    if response then
        local sResponse = response.readAll()
        response.close()
        return sResponse
    end
end

ver = 0;
print("Checking for updates..")
local update = get("jt0nstr4")
if update then
	if fs.exists("remote/version") then
		local file = fs.open("remote/version","r")
		local ver = tonumber(file.readLine())
		file.close()
	end
	if tonumber(update) ~= ver then
		print("Update available.")
		file = fs.open("remote/version","w")
		file.write(update)
		file.close()
		update = get("DDL54jMC")
		if update then
			file = fs.open("butler","w")
			file.write(update)
			file.close()
			print("Update successful.")
		else
			print("Update failed.")
		end
	else
		print("No update available")
	end
else
	print("Networking failed")
end