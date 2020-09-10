<template>
	<div class="container">
		<div id="comments">
			<h1>Comments</h1>
			<table id="Comments">
				<tr>
					<th>Apartment</th>
					<th>Comment</th>
					<th>Rating</th>
					<th>Guest</th>
				</tr>
				<tr v-for="comment in comments">
					<!--<td>{{comment.apartment.name}}</td>-->
					<td>{{comment.text}}</td>
					<td>{{comment.rating}}</td>
					<!--<td>{{comment.guest.username}}</td>-->
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