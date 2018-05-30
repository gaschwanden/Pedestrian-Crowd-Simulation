package MovingAgents;

public class preference {
	public String program;
	public int floorNR;
	public String Type;
	public float size;
	public String ethnic;
	public String Global;
	public String Religion;
	public int preference;

	preference(){

	}

	///////////////////////////////////////////////////////////////////
	//			Constructors
	///////////////////////////////////////////////////////////////////


	preference(String _program, int _floorNR, String _Type, float _size,
			String _ethnic,String _Global,String _Religion){


		program =_program;
		//floorNR =_floorNR;
		//Type =_Type;
		//size =_size;
		ethnic =_ethnic;
		//Global =_Global;
		Religion =_Religion;


	}

	preference(String _program,String _ethnic,String _Religion){


		program =_program;
		//floorNR =_floorNR;
		//Type =_Type;
		//size =_size;
		ethnic =_ethnic;
		//Global =_Global;
		Religion =_Religion;


	}

}
