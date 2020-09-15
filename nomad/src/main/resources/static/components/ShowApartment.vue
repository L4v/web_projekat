<template>
	<form id="apartment">
		<label>Type: {{apartment.type}}</label>
		<label>Number of rooms: {{apartment.noRooms}}</label>
		<label>Number of guests: {{apartment.noGuests}}</label>
		<label>Price: {{apartment.price}}</label>
		<label>Amenities:</label>
		<select id="amenities" multiple disabled>
        	<option v-for="amenity in apartment.amenities" :value="amenity">{{amenity.name}}</option>
        </select>
		<br>
		<div id="comment">
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
	     </div>
	</form>
</template>

<script>
	module.exports = {
		props: ['apartment'],
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

<style scoped>
	#amenities
    {
        height: 100%;
        min-height: 200px;
        min-width: 200px;
    }
</style>