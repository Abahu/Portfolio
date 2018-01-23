id = 4;
local mom = true;
while mom == true do
	local derp = read();
	if derp == "$sd" then 
		mom = false;
	else
		rednet.send(id,derp);
	end
end