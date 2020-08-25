var personalDataVue = new Vue
({
    el: "#personalData",
    data:
    {
        guest: {},
        successMsg: "",
    },
    mounted(){
    	var jwt = localStorage.jwt;
    	
    	if(jwt)
		{
	    	axios.get("rest/get_user",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
		        .then(response => (this.guest = response.data))
	    		.catch(response => 
    			{
    				//TODO(Kristian): handle 404
    				alert("Please log in");
    			});
		}
    },
    	
    methods:
    {
        updatePersonalData: function(guest)
        {
	        axios.post("rest/personal_data", guest)
	        .then(response =>
	            {
	                this.successMsg = "Changes successfully saved.";
	            })
	        .catch(response => {
	        		this.successMsg = "Something's wrong :(";
	        });
        }
    }
});