#!/usr/bin/env python3
"""
Fetches a player's full Hypixel API data and writes it to a pretty-printed JSON file.

Usage:
    python fetch_hypixel_data.py --key YOUR_API_KEY --uuid YOUR_UUID
    python fetch_hypixel_data.py --key YOUR_API_KEY --uuid YOUR_UUID --out my_debug.json

You can also set HYPIXEL_API_KEY / HYPIXEL_UUID as environment variables instead
of passing --key/--uuid, so you don't have to put your key in shell history.
"""

import argparse
import json
import os
import sys

import requests

API_URL = "https://api.hypixel.net/v2/player"


def fetch_player_data(api_key: str, uuid: str) -> dict:
    response = requests.get(API_URL, params={"key": api_key, "uuid": uuid}, timeout=10)
    response.raise_for_status()

    data = response.json()
    if not data.get("success"):
        raise RuntimeError(f"Hypixel API returned an error: {data}")
    if data.get("player") is None:
        raise RuntimeError("No player data returned - check that the UUID is correct.")

    return data


def main():
    parser = argparse.ArgumentParser(description="Fetch and save Hypixel player data as pretty JSON.")
    parser.add_argument("--key", default=os.environ.get("HYPIXEL_API_KEY"), help="Your Hypixel API key")
    parser.add_argument("--uuid", default=os.environ.get("HYPIXEL_UUID"), help="Player UUID (dashed or undashed)")
    parser.add_argument("--out", default="hypixel_player_debug.json", help="Output file path")
    args = parser.parse_args()

    if not args.key or not args.uuid:
        sys.exit("Missing API key or UUID. Pass --key/--uuid or set HYPIXEL_API_KEY/HYPIXEL_UUID env vars.")

    data = fetch_player_data(args.key, args.uuid)

    with open(args.out, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=2, ensure_ascii=False, sort_keys=True)

    print(f"Saved pretty-printed player data to {args.out}")


if __name__ == "__main__":
    main()