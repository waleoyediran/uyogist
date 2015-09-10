"""`main` is the top level module for your Flask application."""

# Import the Flask Framework
from flask import Flask
from flask import jsonify
from flask.globals import request
from models import GistModel

app = Flask(__name__)
# Note: We don't need to call run() since our application is embedded within
# the App Engine WSGI application server.


@app.route('/')
def hello():
    """Return a friendly HTTP greeting."""
    return 'Hello World!'


@app.route('/api/gist', methods=['GET', 'POST'])
def api_gists():
    """Gists Endpoint"""
    if request.method == 'GET':
        data = {'data': 3}
        resp = jsonify(data)
        resp.status_code = 200
        return resp
    elif request.method == 'POST':
        data = request.get_json(force=True)
        gist = GistModel()
        gist.added_by = data['nick']
        gist.gist = data['gist']
        key = gist.put()

        resp = jsonify(dict(gist.to_dict(), **{'id': key.urlsafe()}))
        resp.status_code = 201
        return resp
    else:
        raise RuntimeError("Unimplemented HTTP method")


@app.route('/api/gist/<gist_id>', methods=['GET'])
def api_gist(gist_id):
    gists = {'1': 'gist 1', '2': 'gist 2', '3': 'gist 3'}

    if gist_id in gists:
        return jsonify({gist_id:gists[gist_id]})
    else:
        return not_found()


@app.errorhandler(404)
def not_found(error=None):
    """Return a custom 400 error."""
    message = {
        'status': 404,
        'message': 'Not Found: ' + request.url
    }
    resp = jsonify(message)
    resp.status_code = 404

    return resp


@app.errorhandler(500)
def application_error(e):
    """Return a custom 500 error."""
    message = {
        'status': 500,
        'message': 'Sorry, unexpected error: ' + format(e)
    }
    resp = jsonify(message)
    resp.status_code = 500

    return resp

