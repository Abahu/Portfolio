local programs = {};

local index = 1;
local programPath = "";
if not fs.exists("boot/programs") then 
	fs.makeDir("boot/programs");
end
local file = fs.open("boot/programs","r");

repeat
	programPath = file.readLine();
	if programPath ~= nil and programPath ~= "" then
		programs[index] = programPath;
		index = index + 1;
	end
until programPath == nil or programPath == ""

file.close();

for key in pairs(programs) do
	shell.run(programs[key]);
end

--The butler program *should* never exit, but it if does, shut everything down
os.shutdown();