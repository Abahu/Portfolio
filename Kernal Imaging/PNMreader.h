#include <source.h>

class PNMreader : public Source
{
private:
	char* filepath;
public:
	PNMreader(char* filename);
	virtual void Execute();
	inline virtual void callOut() { std::cerr << "I'm in PNMreader!\n"; }
};
