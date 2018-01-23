#include <sink.h>

class PNMwriter : public Sink
{
private:
public:
	PNMwriter();
	void Write(char* filename);
};
