var showGuestsVue = new Vue
({
    el: "#showGuests",
    data:
    {
        guests: {},
    },
    mounted(){
    	var jwt = localStorage.jwt;
    	
    	if(jwt)
		{
	    	//getMyGuests
		}
    },
    	
    methods:
    {
        searchGuest: function()
        {
            
        }
    }
});