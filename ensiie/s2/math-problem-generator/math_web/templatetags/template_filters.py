from django import template

register = template.Library()


@register.filter
def get_item(array, key):
    if isinstance(array, list) and key < len(array):
        return array[key]
    elif isinstance(array, dict):
        return array.get(key)
    return None
