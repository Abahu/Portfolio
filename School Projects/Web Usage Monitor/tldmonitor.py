"""
tldmonitor.py: Creates an output file of all the top level domains in a given file, along with their relative abundance
Author: Andrew Dalcher

Assignment 10, 13-Oct-16
"""
import argparse


class Date:
    day = 1
    month = 1
    year = 1970
    did_init = False

    def __init__(self, string):
        """
        The constructor for the Date class. Takes a string and parses it

        args:
            self: handled by Python, do not worry
            string: a string that has the date in a (M)M/(D)D/YYYY format
        effects:
            Initialises the Date instance. Assigns new class-level members day, month, and year
            Fails to initialise if the string format is wrong
        raises:
            None
        """
        strs = string.split('/')
        if len(strs) != 3:
            raise ValueError("Date is not formatted as (D)D/(M)M/YYYY! Tried to pass '{}'".format(string))
        nums = []
        for e in strs:
            if not e.isdigit():
                raise ValueError("Date fields are non-numeric! Tried to pass '{}'".format(string))
            nums.append(int(e))
        if 0 > nums[0] > 13: # These exceptions make sure that we don't get bad dates
            raise ValueError("Day field is out of bounds! Tried to pass '{}'".format(string))
        if 0 > nums[1] > 32:
            raise ValueError("Month field is out of bounds! Tried to pass '{}'".format(string))
        if 1970 > nums[2] > 9999:  # There shouldn't be any web pages from before 1970
            raise ValueError("Year field is out of bounds! Tried to pass '{}'".format(string))
        self.month = nums[0]
        self.day = nums[1]
        self.year = nums[2]
        self.did_init = True

    def set(self, string):
        """
        Similar to a initialisation but without changing pointers and wasting memory
        Takes a string and does the same thing as constructor but without creating
        a new object

        args:
            self: handled by Python
            string: a string date in (M)M/(D)D/YYYY format
        returns:
            Boolean value telling if the date was successfully changed. Only used in verbose checking
        effects:
            Changes the value of the Date instance's members based on the new date string
            Fails to change if the string is malformed, and then returns False
        """
        strs = string.split('/')
        if len(strs) != 3:
            return
        nums = []
        for e in strs:
            if not e.isdigit():
                raise ValueError("Date fields are non-numeric! Tried to pass '{}'".format(string))
            nums.append(int(e))
        if 0 < nums[0] < 13:  # These exceptions make sure that we don't get bad dates
            raise ValueError("Date fields are non-numeric! Tried to pass '{}'".format(string))
        if 0 < nums[1] < 32:
            raise ValueError("Date fields are non-numeric! Tried to pass '{}'".format(string))
        if 1970 < nums[2] < 9999:  # There shouldn't be any web pages from before 1970
            raise ValueError("Date fields are non-numeric! Tried to pass '{}'".format(string))
        self.month = nums[0]
        self.day = nums[1]
        self.year = nums[2]
        self.did_init = True

    def __lt__(self, other):
        """
        Less than operator
        args:
            self: handled by Python
            other: another instance of the Date class
        returns:
            Boolean value for if this date is less than other
        """
        if self.year < other.year:
            return True
        elif self.year == other.year:
            if self.month < other.month:
                return True
            elif self.month == other.month:
                if self.day < other.day:
                    return True
                else:
                    return False
            else:
                return False
        else:
            return False

    def __le__(self, other):
        """
        Less than or equal to operator
        args:
            self: handled by Python
            other: another instance of the Date class
        returns:
            Boolean value for if this date is less than or equal to other
        """
        if self.year < other.year:
            return True
        elif self.year == other.year:
            if self.month < other.month:
                return True
            elif self.month == other.month:
                if self.day <= other.day:
                    return True
                else:
                    return False
            else:
                return False
        else:
            return False

    def __gt__(self, other):
        """
        Greater than operator

        args:
            self: handled by Python
            other: another instance of the Date class
        returns:
            Boolean value for if this date is greater than other
        """
        if self.year > other.year:
            return True
        elif self.year == other.year:
            if self.month > other.month:
                return True
            elif self.month == other.month:
                if self.day > other.day:
                    return True
                else:
                    return False
            else:
                return False
        else:
            return False

    def __ge__(self, other):
        """
        Greater than or equal to operator

        args:
            self: handled by Python
            other: another instance of the Date class
        returns:
            Boolean value for if this date is greater than or equal to other
        """
        if self.year > other.year:
            return True
        elif self.year == other.year:
            if self.month > other.month:
                return True
            elif self.month == other.month:
                if self.day >= other.day:
                    return True
                else:
                    return False
            else:
                return False
        else:
            return False

    def __eq__(self, other):
        """
        Equivalence comparison operator

        args:
            self: handled by Python
            other: another instance of the Date class
        returns:
            Boolean value for if this date is equal to other
        """
        return self.year == other.year and self.month == other.month and self.day == other.day

    def is_initialised(self):
        """
        Returns whether or not this instance was given a properly formatted string during initialisation
        In lieu of functional initialisation sequences as seen in C languages, we need some way to deal
        with bad arguments. This is how this class will do it.

        args:
            None
        returns:
            did_init: a variable that stores whether or not this did initialise
        """
        return self.did_init

"""
Note: This is the second method I created for this assignment. The previous one is monitor_complex,
This is the better written one and one that is made for a formatted output file. monitor_complex
Is more robust and can take and output file without line separators.
"""
def monitor(start_date, end_date, file):
    """
    Generates a log file of the percentage TLD connections
    during a certain date range in a certain log file

    Only lines with a date that falls within the specified
    date range are parsed. All others are ignored

    Args:
        start_date: an instance of Date that contains the information for the starting date boundary
        end_date: an instance of Date that contains the information for the ending date boundary
        file:   an opened text file with mode "r", reading to be read. Needs to be formatted with
                (M)M/(D)D/YYYY date in the beginning and the website domain following after, each
                domain on its own line.

    """
    domains = dict()

    for line in file:
        parse = line.strip().split(' ')
        date = Date(parse[0])
        if start_date <= date <= end_date:
            tld = parse[1].split('.')[-1]
            if tld not in domains:
                domains[tld] = 0
            domains[tld] += 1

    total = sum(domains.values())  # Total number of counted domains
    key_list = sorted(domains.keys())
    out_path = "output.out"
    with open(out_path, "w") as out:
        for key in key_list:
            percent = 100 * domains[key] / total
            out.write("{:>5.2f} {}\n".format(percent, key))
    out.close()

"""
This is the first iteration of the monitor function. It is robust and can read correctly output files without line
separation. When I first opened the log files, Notepad didn't show line separation, so I made this function to
accommodate that, only to realise that the '\n' escape code was being appended to each TLD.

Kept to show problem solving, I guess.
"""
def monitor_complex(start_date, end_date, file):
    """
    Generates a log file of the percentage TLD connections
    during a certain date range in a certain log file

    Only lines with a date that falls within the specified
    date range are parsed. All others are ignored

    Args:
        start_date: an instance of Date that contains the information for the starting date boundary
        end_date: an instance of Date that contains the information for the ending date boundary
        file:   an opened text file with mode "r", reading to be read. Needs to be formatted with
                (M)M/(D)D/YYYY date in the beginning and the website domain following after, each
                domain on its own line.

    """
    domains = dict()  # Dictionary that contains the TLD keywords and a number of occurrences as the value
    date = Date(file.read(10))

    parse = ""
    char = "1"

    while char != "":  # Scan list for a digit (indicates the start of a date)
        char = file.read(1)

        # Parse date clause
        if char.isdigit():
            dat = char
            char = file.read(1)
            nchar = file.read(1)
            if char.isdigit() and nchar == "/":
                """
                We have determined that there are two numbers and a forward slash.
                In other words, we have the beginning of a date; no domain contains
                file paths, so it cannot be part of the url.

                The accumulated 'parse' is thus the recorded url, and we may parse it
                """
                # Flush the parse string
                tld = parse[parse.rfind('.')+1:].strip()
                if start_date <= date <= end_date:
                    if tld not in domains:
                        domains[tld] = 0
                    domains[tld] += 1
                    parse = ""

                dat += char + nchar + file.read(7)
                date.set(dat)
            else:
                parse += dat + char + nchar
        else:
            # Add to URL parse
            parse += char
    # Flush the parse string one last time
    tld = parse[parse.rfind('.') + 1:].strip()
    if start_date <= date <= end_date:
        if tld not in domains:
            domains[tld] = 0
        domains[tld] += 1

    total = sum(domains.values())  # Total number of counted domains
    key_list = sorted(domains.keys())
    out_path = "output.out"
    with open(out_path, "w") as out:
        for key in key_list:
            percent = 100 * domains[key] / total
            out.write("{:>5.2f} {}\n".format(percent, key))
    out.close()


def test():
    """
    Date test cases
    """
    jan241998 = Date("01/24/1998")
    jan241998eq = Date("01/24/1998")
    sep112001 = Date("09/11/2001")
    dec312001 = Date("12/31/2001")
    jan012002 = Date("01/01/2002")
    jan012001 = Date("01/01/2001")
    malMonth012001 = Date("0/01/2001")
    janMalDay2001 = Date("01/55/2001")
    jan01MalYear = Date("01/01/01")
    badDate = Date("1/1/1")
    print("Is 01/24/1998 == 01/24/1998: " + str(jan241998 == jan241998eq))
    print("Is 01/24/1998 <= 01/24/1998: " + str(jan241998 <= jan241998eq))
    print("Is 01/24/1998 >= 01/24/1998: " + str(jan241998 >= jan241998eq))
    print("Is 01/01/2002 == 01/01/2001: " + str(jan012002 == jan012001))
    print("Is 01/01/2002 >= 01/01/2001: " + str(jan012002 >= jan012001))
    print("Is 01/01/2002 <= 01/01/2001: " + str(jan012002 <= jan012001))
    print("Is 0/01/2001 instantiated: " + str(malMonth012001.is_initialised()))
    print("Is 01/55/2001 instantiated: " + str(janMalDay2001.is_initialised()))
    print("Is 01/01/01 instantiated: " + str(jan01MalYear.is_initialised()))
    print("Is 1/1/1 instantiated: " + str(badDate.is_initialised()))
    print("Is 01/01/2002 > 12/31/2001: " + str(jan012002 > dec312001))
    print("Is 01/01/2001 < 09/11/2001: " + str(jan012001 < sep112001))
    print("Is 01/01/2002 < 12/31/2001: " + str(jan012002 < dec312001))
    print("Is 01/01/2001 > 09/11/2001: " + str(jan012001 > sep112001))
    print("Is 01/01/2001 < 09/11/2001 < 12/31/2001: " + str(jan012001 < sep112001 < dec312001))
    print("Is 01/01/2001 > 09/11/2001 > 12/31/2001: " + str(jan012001 > sep112001 > dec312001))
    print("Is 01/01/2001 < 09/11/2001 > 12/31/2001: " + str(jan012001 < sep112001 > dec312001))
    print("Is 01/01/2001 > 09/11/2001 < 12/31/2001: " + str(jan012001 > sep112001 < dec312001))


def main():
    """
    Entry point for the script. Gets the start and end date from
    the command line as well as the log file. Passes these to
    monitor()
    input:
        Input is handled at command line
    output:
        None, output is handled in monitor()
    """
    parser = argparse.ArgumentParser(description="Monitor net accesses by TLD")
    parser.add_argument("START", type=str, help="Start date for statistics")
    parser.add_argument("STOP", type=str, help="End date for statistics")
    parser.add_argument("log_file", type=argparse.FileType('r'),
                        help="Name of web log file")
    args = parser.parse_args()
    start_date = Date(args.START)
    end_date = Date(args.STOP)
    log_file = args.log_file
    monitor(start_date, end_date, log_file)
    log_file.close()  # Closing the file should be handled here, not in monitor()


if __name__ == "__main__":
    main()
