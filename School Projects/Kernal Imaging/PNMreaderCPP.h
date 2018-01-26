#include <source.h>

class PNMreaderCPP : public Source
{
private:
	char* filepath;
public:
	PNMreaderCPP(char* filename);
	virtual void Execute();
};
