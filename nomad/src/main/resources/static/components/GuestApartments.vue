<template>
	<div class="container">
		<div id="apartments">
			<h1>Apartments</h1>
			<table>
				 <tr v-for="apartment in apartments">
					<td>{{apartment.type}}</td>
					<td>{{apartment.status}}</td>
					<td>{{apartment.price}}</td>
				</tr>
			</table>
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return {
				apartments: {},
				successMsg: "",
			}
		},
		
		mounted()
		{
	    	var jwt = localStorage.jwt;
	    	
	    	if(jwt)
			{
		    	axios.get("rest/guest_all_apartments", {headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => 
			       	{
			       		this.apartments = response.data;
			       	})
		    		.catch(response => 
	    			{
	    				//TODO(Kristian): handle 404
	    				alert("Please log in");
	    			});
			}
	    },
	    
	    methods:
	    {
			
	    },
    }
</script>