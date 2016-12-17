package javamessagingnetbeans;

/**
 * Employee always needs to go on vacation. Otherwise, other employees wouldn't
 * be concurrent.
 */
class EmployeesVacation
{

    /*
     * Quick relax -> 20 nanoseconds.
     */
    private final int QUICK_RELAX = 20;

    /*
     * Big relax -> 100 nanoseconds.
     */
    private final int BIG_RELAX = 100;

    /*
     * Threshold for noOfNotMessageReceived counter. If noOfNotMessageReceived
     * reach this value, Employee will take QUICK_RELAX.
     */
    private final int NO_OF_NOT_MESSAGE_RECEIVED_WITHOUT_SLEEP_TRESHOLD = 16;

    /*
     * Threshold for noOfNotMessageReceived counter. If noOfNotMessageReceived
     * reach this value, Employee will take BIG_RELAX instead of QUICK_RELAX.
     */
    private final int NO_OF_NOT_MESSAGE_RECEIVED_THRESHOLD = 32;

    /*
     * Limit for not message received. If noOfNotMessage received
     * reach this limit, this employee should terminate it self.
     *
     * If Employee is idle for 10 minutes (600 seconds/600,000,000,000 ns)
     * Employee is considered to use to many resources and Employee should
     * terminate itself. Approximatelly every 10 ns counter is increased by one.
     */
    private final long NO_OF_NOT_MESSAGE_RECEIVED_EXIT = 60000000000L;

    /*
     * Number of time not message received.
     */
    private long noOfNotMessageReceived;

    /**
     * The constructor.
     */
    public EmployeesVacation()
    {
        noOfNotMessageReceived = 0;
    }

    /**
     * Count how many nanoseconds Employees vacation should be.
     *
     * @retVal -1 - this Employee needs to be fired.
     * @retVal 0 - no vacation.
     * @retVal positive value - vacation in nanoseconds.
     */
    public int count()
    {
        noOfNotMessageReceived++;

        if (noOfNotMessageReceived >= NO_OF_NOT_MESSAGE_RECEIVED_EXIT)
        {
            return -1;
        }

        if (noOfNotMessageReceived < NO_OF_NOT_MESSAGE_RECEIVED_WITHOUT_SLEEP_TRESHOLD)
        {
            return 0;
        }

        return (noOfNotMessageReceived < NO_OF_NOT_MESSAGE_RECEIVED_THRESHOLD)
            ? QUICK_RELAX
            : BIG_RELAX;
    }

    /**
     * Reset his vacation.
     */
    public void reset()
    {
        noOfNotMessageReceived = 0;
    }
}
