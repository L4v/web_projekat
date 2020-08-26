var amenitiesVue = new Vue
({
    el: "#amenities",
    data:
    {
        amenities: {},
        successMsg: "",
    },
    
    mounted(){
    	var jwt = localStorage.jwt;
    	
    	if(jwt)
		{
	    	axios.get("rest/admin_all_amenities",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
		        .then(response => (this.amenities = response.data))
	    		.catch(response => 
    			{
    				//TODO(Kristian): handle 404
    				alert("Please log in");
    			});
		}
    },
    
    methods:
    {
        
    }
})