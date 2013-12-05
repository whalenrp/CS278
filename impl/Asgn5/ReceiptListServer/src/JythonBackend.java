
public class JythonBackend
{
	private static final String PYTHONPATH = "../../mongo-files:/usr/local/lib/python2.7/dist-packages/nose-1.3.0-py2.7.egg:"
			+ "/usr/lib/python2.7:/usr/lib/python2.7/plat-linux2:/usr/lib/python2.7/lib-tk:/usr/lib/python2.7/lib-old:"
			+ "/usr/lib/python2.7/lib-dynload:/usr/local/lib/python2.7/dist-packages:/usr/lib/python2.7/dist-packages";
	
	private static final String[] sysPathAppends = PYTHONPATH.split(":");
	
	private static final JythonObjectFactory accountsFactory = new JythonObjectFactory(IAccountsWrapper.class,"db_interface","AccountsWrapper",sysPathAppends);
	
	private static final JythonObjectFactory receiptsFactory = new JythonObjectFactory(IReceipt.class, "db_interface", "Receipt", sysPathAppends);
	
	public static JythonObjectFactory createAccountsWrapper()
	{
        return accountsFactory;
	}
	
	public static JythonObjectFactory getReceiptFactory()
	{
		return receiptsFactory;
	}
}
