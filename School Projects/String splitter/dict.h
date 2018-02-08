#include <string>
#include <vector>
#include <functional>

class Dict
{
private:
	std::vector<std::string> data;
	static std::hash<std::string> strHash;
public:
	Dict();
	void addString(std::string s);
	bool isIn(std::string s);
};
