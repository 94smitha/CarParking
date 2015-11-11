package main;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class ExitBarrier extends EntranceTicket
{
    static boolean prePaid;

    static double priceTemp = 0;

    static String yesOrNo;

    static int lineNumber;

    static Date entryTime = null;

    static Date exitTime = new Date();

    static int hours;

    static int minutes;

    static int differenceTimes = hours;

    static int differenceMinutes;

    static int hoursBooked;

    static int differenceHours;

    static int hoursToCharge;

    static int discountPrice;

    static String regNo;

    static boolean overstayed;

    static String cardNo;

    static boolean fail;

    static String reason;

    static Date expiry;

    public static void exit() throws IOException, ParseException
    {
        // initialises variables
        Scanner keyboard = new Scanner(System.in);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        int lengthOfStay;
        int moneyOwed;
        String expDate;

        DecimalFormat twoDP = new DecimalFormat("##.##");

        // user enters reg Number
        System.out.println();
        System.out.print("Enter your Reg Number");
        regNo = keyboard.nextLine().toUpperCase();
        System.out.println(regNo);

        // read number plate from EntranceTicket class

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
                {
                    if (line.contains(regNo))

                    {
                        lineNumber = lineNum;
                        // splits string number plate is found on at tab and reads whether it is prepaid or not
                        String[] details = line.split("\t");

                        // finds entry time and assigns to variable
                        entryTime = dateFormat.parse(details[1]);

                        System.out.println("Arrived at: " + dateFormat.format(entryTime));
                        System.out.println("Departing at: " + dateFormat.format(exitTime));

                        // finds the difference between the entry and exit time
                        long secs = (exitTime.getTime() - entryTime.getTime()) / 1000;
                        hours = (int) (secs / 3600);
                        minutes = (int) ((secs % 3600) / 60);

                        if (minutes > 0)
                        {
                            hoursToCharge = hours + 1;
                        }

                        else if (minutes == 0)
                        {
                            hoursToCharge = hours;
                        }

                        System.out.println("Stayed for: " + hoursToCharge + " hours");

                        yesOrNo = details[3];

                        // if it is prepaid, set prepaid boolean to true, if not, set to false
                        if (yesOrNo.equals("yes"))
                        {
                            prePaid = true;

                        }
                        else if (yesOrNo.equals("no"))
                        {
                            prePaid = false;
                        }

                    }
                }
            }
            // scan.close();
        }

        catch (ParseException e)
        {
            e.printStackTrace();
        }
        finally
        {
        }

        try
        {
            File file = new File("C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\PreBookedData.txt");
            Scanner scan = new Scanner(file);

            int lineNum = 0;
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                lineNum++;

                if (line != null)
                {
                    if (line.contains(regNo))
                    {

                        // reads hours stayed from only the line that the number plate was stored on and sets to a
                        // variable
                        int readLine = lineNum;
                        String[] details = line.split("\t");
                        hoursBooked = Integer.parseInt(details[1]);
                        System.out.println("You have pre-paid for this ticket for " + hoursBooked + " hours");

                        differenceHours = (hoursToCharge - hoursBooked);
                        if (differenceHours > 0)
                        {
                            overstayed = true;
                            System.out.print("You have overstayed by " + differenceHours + " hours. ");
                            Calendar mydate = Calendar.getInstance();
                            int dow = mydate.get(Calendar.DAY_OF_WEEK);
                            boolean isWeekday = ((dow >= Calendar.MONDAY) && (dow <= Calendar.FRIDAY));
                            // if it is a weekday, calls the weekday method to work out the price
                            if (isWeekday == true)
                            {
                                WeekdayPrices.weekday();
                                discountPrice = Integer.parseInt(twoDP
                                        .format(((WeekdayPrices.price) - (WeekdayPrices.price) / 10)));
                                System.out.println("An additional charge of £ " + discountPrice + " is required");
                            }
                            // if it is a weekend, calls the weekend method to work out the price
                            else
                            {
                                WeekendPrices.weekend();
                                discountPrice = Integer.parseInt(twoDP
                                        .format(((WeekendPrices.price) - (WeekendPrices.price) / 10)));
                                System.out.println("An additional charge of £" + discountPrice + " is required");

                            }
                            // asks the user to enter their card number
                            System.out.println("\nEnter card number:");
                            cardNo = keyboard.next();
                            // ensures that the card number must be 16 digits in length
                            while (cardNo.length() != 16)
                            {
                                fail = true;
                                reason = "Incorrect Card Number";
                                System.out.println("Card numbers must be 16 digits long.\nPlease enter card number.");
                                cardNo = keyboard.next();
                            }
                            // asks the user to enter their expiry date in a mm/yy format
                            System.out.println("Please enter your expiration date in the format mm/yy");
                            String input = keyboard.next();
                            // checks whether the date entered is before the current date
                            SimpleDateFormat simpleDateFormatPay = new SimpleDateFormat("MM/yy");
                            dateFormat.setLenient(false);
                            try
                            {
                                expiry = simpleDateFormatPay.parse(input);
                            }
                            catch (ParseException e)
                            {
                                e.printStackTrace();
                            }
                            boolean expired = expiry.before(new Date());
                            // if the card has expired, tells the user this and allows them to re-enter
                            while (expired == true)
                            {
                                fail = true;
                                reason = "Card expired";
                                System.out
                                        .println("This card has already expired.\nPlease enter your expiry date in the format mm/yy");
                                input = keyboard.next();
                                dateFormat = new SimpleDateFormat("MM/yy");
                                dateFormat.setLenient(false);
                                try
                                {
                                    expiry = dateFormat.parse(input);
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                }
                                expired = expiry.before(new Date());
                            }
                            System.out.println("Thank you. Your card has been charged £" + discountPrice);

                        }
                        else
                        {
                            overstayed = false;
                            discountPrice = 0;
                        }

                    }
                }
            }
            scan.close();
        }
        finally
        {
        }

        try
        {

            if (prePaid == false)
            {
                Calendar mydate = Calendar.getInstance();
                int dow = mydate.get(Calendar.DAY_OF_WEEK);
                boolean isWeekday = ((dow >= Calendar.MONDAY) && (dow <= Calendar.FRIDAY));
                // if it is a weekday, calls the weekday method to work out the price
                if (isWeekday == true)
                {
                    WeekdayPrices.weekday();
                    System.out.println("Total price is £" + WeekdayPrices.price + "0");
                    priceTemp = WeekdayPrices.price;
                }
                // if it is a weekend, calls the weekend method to work out the price
                else
                {
                    WeekendPrices.weekend();
                    System.out.println("Total price is £" + WeekendPrices.price + "0");
                    priceTemp = WeekendPrices.price;
                }

                // asks the user to enter their card number
                System.out.println("\nEnter card number:");
                cardNo = keyboard.next();
                // ensures that the card number must be 16 digits in length
                while (cardNo.length() != 16)
                {
                    fail = true;
                    reason = "Incorrect card number";
                    System.out.println("Card numbers must be 16 digits long.\nPlease enter card number.");
                    cardNo = keyboard.next();
                }
                // asks the user to enter their expiry date in a mm/yy format
                System.out.println("Please enter your expiration date in the format mm/yy");
                String input = keyboard.next();
                // checks whether the date entered is before the current date
                SimpleDateFormat simpleDateFormatPay = new SimpleDateFormat("MM/yy");
                dateFormat.setLenient(false);
                try
                {
                    expiry = simpleDateFormatPay.parse(input);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                boolean expired = expiry.before(new Date());
                // if the card has expired, tells the user this and allows them to re-enter
                while (expired == true)
                {
                    fail = true;
                    reason = "Card expired";
                    System.out.println("This card has already expired.\nPlease enter a new card");
                    System.out.println("\nEnter card number:");
                    cardNo = keyboard.next();
                    // ensures that the card number must be 16 digits in length
                    while (cardNo.length() != 16)
                    {
                        fail = true;
                        reason = "Incorrect card number";
                        System.out.println("Card numbers must be 16 digits long.\nPlease enter card number.");
                        cardNo = keyboard.next();
                    }
                    System.out.println("Please enter your expiration date in the format mm/yy");
                    input = keyboard.next();
                    dateFormat = new SimpleDateFormat("MM/yy");
                    dateFormat.setLenient(false);
                    try
                    {
                        expiry = dateFormat.parse(input);
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    expired = expiry.before(new Date());
                }
                System.out.println("Thank you. Your card has been charged £" + priceTemp + "0");

                if (prePaid == true)
                {
                    System.out.println(hours);

                }

            }
        }
        finally
        {
        }

    }

}
