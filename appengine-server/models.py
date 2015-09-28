__author__ = 'oyewale'

"""
models.py
App Engine datastore models
"""


from google.appengine.ext import ndb


class GistModel(ndb.Model):
    """Gist Model"""
    gist = ndb.StringProperty(required=True)
    image = ndb.BlobKeyProperty()
    added_by = ndb.StringProperty(required=True)
    timestamp = ndb.DateTimeProperty(auto_now_add=True)