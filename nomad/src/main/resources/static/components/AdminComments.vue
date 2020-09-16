<template>
	<div class="container">
		<div id="comments">
			<h1>Comments</h1>
			<table id="Comments">
				<tr>
					<th>Guest username</th>
					<th>Comment</th>
					<th>Rating</th>
					<th>Apartment ID</th>
				</tr>
				<tr v-for="comment in comments">
					<td>{{comment.guestId}}</td>
					<td>{{comment.text}}</td>
					<td>{{comment.rating}}</td>
					<td>{{comment.apartmentId}}</td>
				</tr>
			</table>
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return{
				comments: {},
			}
		},
		
		mounted(){
	    	 var jwt = localStorage.jwt;
	    	 if(jwt)
	    	 {
	         	axios.get("rest/admin_view_comments", {headers:{"Authorization": "Bearer " + jwt}})
		             .then(response =>
		             {
		                 this.comments = response.data;
		             })
		             .catch(response =>
		             {
		                 
		             });
		     }
	    },
	}
</script>