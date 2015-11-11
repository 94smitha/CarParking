package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AutorisationFile
{
    static int transNo = 0;

    static String transType;

    static String cardNo;

    static boolean fail;

    static String reason;

    static Date expiry;

    public static void authorisation() throws IOException
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DateFormat expiryFormat = new SimpleDateFormat("MM/yy");
        Date date = new Date();

        try
        {
            File f = new File("C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\AuthorisationFile.txt");
            Scanner sc = new Scanner(f);

            while (sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] details = line.split(",");
                transNo = Integer.parseInt(details[0]);

            }
            sc.close();

        }
        finally
        {
        }

        if (RunSystem.choice == 1)
        {
            transType = "P";
            cardNo = PreBookedTicket.cardNo;
            fail = PreBookedTicket.fail;
            reason = PreBookedTicket.reason;
            expiry = PreBookedTicket.expiry;
        }
        else if (RunSystem.choice == 3)
        {
            cardNo = ExitBarrier.cardNo;
            fail = ExitBarrier.fail;
            reason = ExitBarrier.reason;
            expiry = ExitBarrier.expiry;

            if (ExitBarrier.overstayed = true)
            {
                transType = "O";
            }
            else if (ExitBarrier.overstayed = false)
            {
                transType = "D";
            }
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                "C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\AuthorisationFile.txt", true))))
        {
            out.print((transNo + 1) + ",");
            out.print(transType + ",");
            out.print(cardNo + " ");
            out.print(expiryFormat.format(expiry) + ",");
            out.print(dateFormat.format(date) + ",");
            if (fail = true)
            {
                out.print("fail" + ",");
                out.println(reason);
            }
            else if (fail = false)
            {
                out.print("pass" + ",");
                out.println("n/a");
            }

        }

    }
}
