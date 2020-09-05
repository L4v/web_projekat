<template>
	<div class="container">
		<div id="apartments">
			<h1>Apartments</h1>
			<table>
				 <tr v-for="apartment in apartments">
					<td>{{apartment.type}}</td>
					<td>{{reservation.location}}</td>
					<td>{{reservation.price}}</td>
				</tr>
				<tr>
					<td><button v-on:click="removaAll()">Remove all</button></td>
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
		    	axios.get("rest/admin_view_apartments",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => (this.apartments = response.data))
		    		.catch(response => 
	    			{
	    				//TODO(Kristian): handle 404
	    				alert("Please log in");
	    			});
			}
	    },
	    
	    methods:
	    {
			removeAll: function()
	     	{
	     		axios.post("rest/admin_remove_apartments")
		        .then(response =>
		            {
		                this.successMsg = "Apartments successfully removed.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed removing apartments";
		        });
	     	}   
	    }
</script>