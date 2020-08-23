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
	    	axios.get("rest/getUser",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
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
            let g = 
                {
                    username: guest.username,
                    password: guest.password,
                    name:     guest.name,
                    surname:  guest.surname,
                    sex:      guest.sex,
                    userType: guest.userType,
                };
            const vm = this;
            axios.post("rest/personal_data", g)
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