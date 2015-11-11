package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CentralLogFile
{
    static boolean prePaid;

    static double preBookedPrice;

    public static void centralLog() throws IOException, ParseException
    {

        String regNo = ExitBarrier.regNo;

        int transNo = 0;
        Date entryTime = null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        System.out.println(regNo);

        try
        {
            File file = new File("C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\EntranceTicketData.txt");
            Scanner scan = new Scanner(file);

            int lineNum = 0;
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                lineNum++;

                if (line != null)
                    ;
                {

                    if (line.contains(regNo))

                    {
                        int lineNumber = lineNum;
                        // splits string number plate is found on at tab
                        String[] details = line.split("\t");

                        // finds entry time and assigns to variable
                        transNo = Integer.parseInt(details[0]);
                        entryTime = dateFormat.parse(details[1]);
                    }
                }
            }
        }
        finally
        {
        }

        try
        {
            File file = new File("C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\PreBookedData.txt");
            Scanner scanner = new Scanner(file);

            int lineNum = 0;
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                lineNum++;

                if (line != null)
                {
                    // if number plate is present, sets pre-paid boolean to true
                    if (line.contains(regNo))
                    {
                        int readLine = lineNum;
                        String[] details = line.split("\t");

                        preBookedPrice = Integer.parseInt(details[2]);
                        // reads hours stayed from only the line that the number plate was stored on and sets to a
                        // variable

                    }
                    else
                        preBookedPrice = 0;
                }
                {

                }
                // scanner.close();
            }
        }

        finally
        {
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                "C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\CentralLogFile.txt", true))))
        {
            out.print(transNo + ",");
            out.print(regNo + ",");
            out.print(dateFormat.format(entryTime) + ",");
            out.print(dateFormat.format(ExitBarrier.exitTime) + ",");
            out.print(ExitBarrier.hours + ":");
            out.print(ExitBarrier.minutes + ",");
            out.println((preBookedPrice + ExitBarrier.priceTemp + ExitBarrier.discountPrice));

        }

    }
}
