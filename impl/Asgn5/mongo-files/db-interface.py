import pymongo
from pymongo import MongoClient

#guest_user = {"username":"guest", "password":"guest"}

class AccountsWrapper():
	"""
	This class will be a wrapper for the pymongo internals of managing accounts and their
	corresponding data from the MobileReceipts project.
	"""
	def __init__(self):
		 self.client = MongoClient()
		 self.db = self.client.test
		 self.users = self.db.users
		 
	
	def are_valid_credentials(