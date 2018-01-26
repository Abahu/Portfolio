#include <sink.h>

class PNMwriterCPP : public Sink
{
private:
public:
	PNMwriterCPP();
	void Write(char* filename);
};
