#include <logging.h>
#include <cstdio>
#include <cstring>

FILE* Logger::logger = NULL;

DataFlowException::DataFlowException(const char* type, const char* error)
{
	strcpy(msg, type);
	strcat(msg, ": Error: ");
	strcat(msg, error);
}

void Logger::LogEvent(const char* event)
{
	if(logger == NULL)
		logger = fopen("logger", "w");
	fprintf(logger, "%s\n", event);
	fflush(logger);
}

void Logger::Finalize()
{
	if(logger != NULL)
	{
		fflush(logger);
		fclose(logger);
	}
}
