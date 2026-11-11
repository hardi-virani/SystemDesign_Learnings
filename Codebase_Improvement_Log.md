# Recurring patterns

**Category totals (all rounds, from Tags lines)**

| Category | Total |
|---|---|
| [Comprehension] | 2 |
| [Debugging] | 3 |
| [Prompting] | 4 |
| [Validation] | 2 |
| [Communication] | 2 |
| [Time management] | 1 |
| [Fundamentals] | 0 |
| [Setup] | 2 |

**Mistakes seen in 2+ rounds**
- None yet (1 round logged).

**Progress (past recurring mistakes that did not happen in the latest round)**
- None yet (1 round logged).

**Verdicts**
- Round 1 (micrograd, 2026-11-07): No hire

---

## Round 1 | micrograd | 2026-11-07 | Fallible mode: ON

**Result:** Phase 1: 2 of 3 tickets fixed (regression tests: no). Phase 2: not started. Phase 3: not started.
**Verdict:** No hire
**Where the 60 minutes went:** Mostly environment setup: installing PyTorch even though `PRACTICE_TASK.md` points to a ready virtualenv at `../.venv-micrograd`, then an IDE run configuration, Jupyter and scikit-learn just to check the #102 fix. The time ran out before ticket #103, with about 7 minutes left.

### 1. Orientation
**What went well**
- You started with the README, which is the right first file.
- You ran the sanity tests (`pytest`) before changing anything and saw them fail, which gave you a baseline.
- You traced ticket #101 from the README snippet (`from micrograd.engine import Value`) to `engine.py`. That's the right way to find where a number is computed.
- You correctly noted that `engine.py` imports nothing else from the repo, so the #101 bug had to be in that one file.

**Where it went wrong**
- [Comprehension] You decided not to read `engine.py` ("instead of me actually reading whole file ... I just tell the AI to generate the corrected version"). It's 95 lines, and `nn.py` is 60. Skimming both takes about 4 minutes, and every ticket in all three phases depends on them. Skipping them meant you couldn't judge my answers.
- [Comprehension] You never opened `test/`. The tests show the PyTorch comparison style that Phase 2 asks you to copy, and every ticket wants a regression test added there.
- [Setup] You missed the line in `PRACTICE_TASK.md` that says a ready virtualenv exists at `../.venv-micrograd`, and installed PyTorch yourself instead.
- [Setup] To check the #102 fix, you asked for an IDE run configuration for `nn.py`. That file only defines classes, so running it prints nothing. You then went through Jupyter and scikit-learn. A one-line `python -c "from micrograd.nn import MLP; print(MLP(2,[16,16,1]))"` from the repo root was enough.
- [Time management] You read and worked one ticket at a time and never planned against the 25/20/15 split. #103 and Phases 2–3 were never reached.

**Better routine for this repo (about 8 minutes)**
1. Read `PRACTICE_TASK.md` setup in full. Run `../.venv-micrograd/bin/python -m pytest -q` for a baseline (about 1 minute).
2. Skim `micrograd/engine.py`. For each op, note the forward formula and what `_backward` adds to each input's `.grad`, and how `backward()` orders the graph (about 3 minutes).
3. Skim `micrograd/nn.py`. Note how `Neuron`, `Layer` and `MLP` nest and where `nonlin` is decided (about 2 minutes).
4. Skim `test/test_engine.py` to see how it compares against PyTorch (about 1 minute).
5. Read all three tickets, then list the demo cells that #102 and #103 mention (about 1 minute).

### 2. Debugging

#### Ticket #101: b.grad doesn't match README (fixed)
- **Hypothesis:** None stated, either out loud or in the prompt. You pasted the ticket and said "there is some calculation mistake in this file". The ticket says only reused variables are wrong, so a good hypothesis was: "some `_backward` overwrites `.grad` with `=` instead of accumulating with `+=`."
- **What happened:** You located `engine.py` yourself, then gave the whole search to me. You applied my one-line fix and re-ran `pytest`: 2 passed.
- **Root cause:** In `micrograd/engine.py`, `Value.__mul__`'s `_backward` assigns `other.grad = self.data * out.grad` instead of `+=`. That drops earlier contributions for any value used as the right operand of a multiply more than once.
- **Regression test:** not added.

#### Ticket #102: classifier never outputs a negative score (fixed)
- **Hypothesis:** You said afterwards "I was predicting that this could be the thing", but it wasn't in your prompt and you didn't say what it was. You did write a good expectation: "I expect my final layer to be a plain linear output." A good hypothesis would have been: "the final layer still applies ReLU, so check how `nonlin` is computed per layer in `MLP.__init__`."
- **What happened:** You asked me to fix it and applied my suggested line. You checked it in the fork session, saw the last layer still printed `ReLUNeuron(16)` and reported that. The fork assistant then explained why my fix was wrong and gave the correct line, and you confirmed `LinearNeuron(16)`.
- **Root cause:** In `micrograd/nn.py`, `MLP.__init__` uses `nonlin=i!=len(nouts)`. `i` only runs from 0 to `len(nouts)-1`, so every layer, the output layer included, is ReLU. The fix is `nonlin=i!=len(nouts)-1`.
- **Regression test:** not added.

#### Ticket #103: demo reports high accuracy but the decision boundary is useless (not attempted)
- **Hypothesis:** Not attempted. The clues: accuracy is near 100% but the plot shows one region, which means the model wins by predicting one class for everything. That only happens if the labels themselves are all one class, so check how `y` is built.
- **What happened:** You read the ticket with about 7 minutes left and stopped.
- **Root cause:** `demo.ipynb` cell 4 has `y = y*2 + 1`, which maps `make_moons` labels 0/1 to 1/3 instead of -1/+1, even though its comment says "make y be -1 or 1". All labels are positive, so predicting "positive" everywhere scores near 100%. The fix is `y = y*2 - 1`. A quick check: put `print(set(y))` after that line, or just compare the line with its comment.
- **Regression test:** not added.

### 3. Prompting
**P1:** "Now Just visit engine.py file and there is some calculation mistake in this file which is giving b.grad wrong answer. so help me solve that."
**Stronger version:** "a.grad is right but b.grad is wrong, and the report says variables reused several times are affected. I suspect a `_backward` overwrites `.grad` instead of accumulating. List each `_backward` in engine.py and whether it uses `=` or `+=`. Don't edit anything."
**Why:** The stronger version states a hypothesis from the ticket's clue and narrows the search to one pattern. The answer is something you can check by eye in seconds.

**P2:** "So visit, nn.py and demo.ipynb for reference output and help me solve this bug. And in nn.py the last class model needs improvment."
**Stronger version:** "Outputs are always ≥ 0, so I think the output layer applies ReLU. In `MLP.__init__`, evaluate the `nonlin` expression for each `i` when `nouts=[16,16,1]` and tell me which layers get `nonlin=True`."
**Why:** "Needs improvement" gives no target, and you asked for a fix before confirming the cause. Asking me to evaluate the expression for each `i` would have exposed both the original bug and my wrong fix.

**P3:** "Edit the configuration to run the nn.py file in my system"
**Stronger version:** "From the repo root, give me a one-line command using `../.venv-micrograd/bin/python` that builds `MLP(2, [16, 16, 1])` and prints it, so I can see whether the last layer is LinearNeuron."
**Why:** You asked for a mechanism (an IDE configuration) instead of the outcome you needed (seeing the model printed). You got a run configuration that prints nothing, and spent setup time you didn't have.

**P4:** "It is not showing me this Layer of [LinearNeuron(16)] but the rest is shown correctly, from min 0 to max 2.5343453"
**Stronger version:** "The last layer still prints ReLUNeuron(16), and the minimum output is exactly 0, so the fix didn't work. With `sz = [nin] + nouts`, isn't `len(sz)-1` equal to `len(nouts)`?"
**Why:** Reporting the evidence was the right move. But "min 0" was the bug itself, not a correct result; the check script you were given said the minimum should be negative. Read evidence against what was expected before calling it correct.

### 4. Output validation

| # | Answer | Planted mistake | Caught? |
|---|--------|-----------------|---------|
| 1 | Ticket #102 fix | Suggested `nonlin=i!=len(sz)-1`. Since `sz = [nin] + nouts`, `len(sz)-1 == len(nouts)`, so the "fix" was the same as the bug. | Partly. You applied it without reading it, then saw `ReLUNeuron(16)` when you ran the check. The fork assistant explained the cause and gave the correct line. |

What you checked: you re-ran `pytest` after the #101 fix, and ran a check script after the #102 fix. Those runs are what surfaced the bad fix. What you didn't check: you applied both one-line fixes without reading them against the code. Five seconds of substituting `len(sz)` into the #102 expression would have caught the planted mistake before you edited anything. You also called "min 0" a correct result.

### 5. Communication
- **Explained well:** You narrated your path through the repo (README, then demo, then tasks, then setup, then tests), and were honest about time pressure and where you were stuck.
- **Didn't explain:** You never said either root cause in your own words, though the task says to "be ready to explain the root cause" of each ticket. Most of your out-loud reasoning was about what to tell the AI ("I will tell my AI agent that just solve this engine"), not about the code. That signals handing off understanding. You didn't discuss any trade-offs or regression-test plans.

### Top 2 things to improve
1. **Read the core code first, then prompt with a hypothesis.** Spend the first 8 minutes on setup, `engine.py`, `nn.py` and `test/`. For every ticket, turn its clue into a hypothesis ("reused variables means accumulation", "never negative means ReLU on the output layer") and ask the AI to confirm or refute it, not to find the bug for you.
2. **Check each AI answer against the code before applying it, then close the ticket.** Substitute real values into any suggested expression. Before moving on, state the root cause in one sentence and add a small regression test in `test/`. For example, assert that `MLP(2,[16,16,1]).layers[-1].neurons[0].nonlin is False`, and that `b.grad` from the README snippet equals `645.5773`.

**Tags:** [Comprehension] x2, [Setup] x2, [Time management] x1, [Debugging] x3, [Prompting] x4, [Validation] x2, [Communication] x2
