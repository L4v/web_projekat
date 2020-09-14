<template>
	<form id="apartment">
		<textarea v-model="commentText" rows="6" cols="30" placeholder="Comment..."></textarea>
		<select name="rating" v-model="rating" required>
	    	<option value="" disabled>Rating</option>
	    	<option value="1">1</option>
	    	<option value="2">2</option>
	    	<option value="3">3</option>
	    	<option value="4">4</option>
	    	<option value="5">5</option>
	     </select>
	     <button class="button-primary" @click="addComment()">Comment</button>
		Type: {{apartment.type}}
		Number of rooms: {{apartment.noRooms}}
		Number of guests: {{apartment.noRooms}}
		Price: {{apartment.price}}
	</form>
</template>

<script>
	module.exports = {
		data: function()
		{
			return {
				apartment: {},
				successMsg: "",
				commentText: "",
				rating: "",
				id: "",
			}
		},

		mounted()
		{
	    	/*var jwt = localStorage.jwt;
	    	
	    	if(jwt)
			{
		    	axios.get("rest/guest_reserved_apartments", {headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => 
			       	{
			       		this.apartments = response.data;
			       	})
		    		.catch(response => 
	    			{
	    				//TODO(Kristian): handle 404
	    				alert("Please log in");
	    			});
			}*/
	    },
	    
	    methods:
	    {    
			addComment: function()
			{
				let comment = 
				{
					id: Math.random(),
					text: this.commentText,
					rating: this.rating,
					apartmentId: this.apartment.id,
					guestId: "",
				};
				
				axios.post("rest/guest_add_comment", comment, {headers:{"Authorization": "Bearer " + localStorage.jwt}})
		        .then(response =>
		            {
		                this.successMsg = "Comment successfully added.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed adding comment";
		        });
			}
	    },
    }
</script>