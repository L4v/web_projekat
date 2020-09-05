<template>
	<div class="container">
		<div id="reservations">
			<h1>Reservations</h1>
			<table>
				 <tr v-for="reservation in reservations">
					<td>{{reservation.startDate}}</td>
					<td>{{reservation.noDays}}</td>
					<td>{{reservation.totalPrice}}</td>
					<td>{{reservation.status}}</td>
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
				reservations: {},
				successMsg: "",
			}
		},
		
		mounted()
		{
	    	var jwt = localStorage.jwt;
	    	
	    	if(jwt)
			{
		    	axios.get("rest/admin_view_reservations",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => (this.reservations = response.data))
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
</script>