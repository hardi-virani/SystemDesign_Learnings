# Recurring patterns

**Category totals (all rounds, from Tags lines)**

| Category | R1 | R2 | Total |
|---|---|---|---|
| [Comprehension] | 2 | 2 | 4 |
| [Debugging] | 3 | 2 | 5 |
| [Prompting] | 4 | 2 | 6 |
| [Validation] | 2 | 3 | 5 |
| [Communication] | 2 | 2 | 4 |
| [Time management] | 1 | 1 | 2 |
| [Fundamentals] | 0 | 1 | 1 |
| [Setup] | 2 | 0 | 2 |

**Mistakes seen in 2+ rounds**
- Oriented without reading the core source (`engine.py`, `nn.py`, demo cells): Rounds 1, 2. In R1 you skipped it; in R2 you oriented from the README prose instead of the code.
- Skipped `test/` during orientation: Rounds 1, 2.
- Accepted an AI fix without checking it against the code or the expected outcome: Rounds 1, 2. R1: the no-op `len(sz)-1` fix. R2: the no-op `contourf` levels fix, plus the missing `__repr__` update.
- Misread evidence that contradicted success: Rounds 1, 2. R1: "min 0" called correct. R2: a one-color plot and "100% from step 1" called a pass.
- No regression tests added: Rounds 1, 2.
- Phase 3 never reached: Rounds 1, 2.
- Vague, location-presuming prompts ("help me solve that", "check that out"): Rounds 1, 2.

**Progress (past recurring mistakes that did not happen in the latest round)**
- Stated hypotheses out loud and in prompts (R1: none for #101 or #102). R2's hypotheses were wrong, but they were stated and tested.
- No environment or setup thrash (R1: PyTorch install, IDE config, Jupyter, scikit-learn).
- Did a deliberate orientation pass with written per-file notes before the first ticket (R1: went straight to tickets).
- Narrated as in an interview, not about what to tell the AI (R1: "I will tell my AI agent that just solve this engine").
- Reached Phase 2 and shipped a tanh op and 4 PyTorch-checked tests (R1: Phase 2 not started).

**Verdicts**
- Round 1 (micrograd, 2026-11-07): No hire
- Round 2 (micrograd, 2026-11-11): Lean no hire

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

---

## Round 2 | micrograd | 2026-11-11 | Fallible mode: ON

**Result:** Phase 1: 2 of 3 tickets fixed (regression tests: no). Phase 2: partial. Phase 3: not started.
**Verdict:** Lean no hire
**Where the 60 minutes went:** The longest stretches were the README walkthrough at the start and ticket #103: two hypotheses, then a notebook run in the fork session. Phase 2 went quickly because the AI wrote the code, and the round ended before Phase 3.

### 1. Orientation
**What went well**
- You acted on Round 1's top feedback. You opened with a deliberate orientation pass, read the README and `setup.py`, and wrote down on paper what each file does before touching a ticket.
- Your summaries were accurate at the level the README describes: `engine.py` is a scalar autograd engine (reverse-mode over a dynamically built DAG), and `nn.py` builds the neural-net library on top of it. You also linked the demo to the moons decision-boundary image.
- You stopped narrating to me and switched to talking as you would in an interview ("from now on I will talk on how exactly I will talk in an interview").

**Where it went wrong**
- [Comprehension] You oriented from the README's prose, not from the code. You never opened `engine.py`, `nn.py` or the demo's cells during orientation. The README describes what the code is meant to do; the bugs are where the code differs from that. Reading cell 4 next to its own comment ("make y be -1 or 1") would have shown the #103 bug.
- [Comprehension] You skipped `test/` again: "it is only for the sanity check, which I've already done in the previous round". That's the same as Round 1. The tests are where Phase 2 part 3 and every regression test go, and you needed to know their style 30 minutes later.
- [Time management] Most of the orientation time went to restating the README out loud, sentence by sentence, without new information about the code. That time was gone when Phase 3 came up.

**Better routine for this repo (about 8 minutes)**
1. Run `../.venv-micrograd/bin/python -m pytest -q` for a baseline (about 1 minute).
2. Read `micrograd/engine.py` top to bottom. For each op, write forward formula / local derivative / uses `+=`? (about 3 minutes).
3. Read `micrograd/nn.py`. Write `Neuron` → `Layer` → `MLP`, where `nonlin` is set, and what `__repr__` prints (about 2 minutes).
4. Skim `test/test_engine.py` for its pattern (same expression in micrograd and torch, compare `.data` and `.grad`) (about 1 minute).
5. Skim `demo.ipynb` cell by cell. Check each comment against the line below it, especially data prep (cell 4), loss (cell 6) and the plot (cell 8) (about 1 minute).

### 2. Debugging

#### Ticket #101 and #102 (fixed in Round 1, not revisited)
- **Regression test:** not added for either ticket. You asked out loud "What is regression testing?", which is a [Fundamentals] gap; see Top 2 below.

#### Ticket #103: demo shows ~100% accuracy but a one-region decision boundary (not fixed)
- **Hypothesis:** You stated two, which is real progress from Round 1. First: "I suspect that there is an issue with the parameters function in the both the class, Neuron and Layer". Second: "the visual decision boundary is where the problem stands". Neither fits the evidence. A bug in how parameters are collected would hurt training and lower accuracy, not produce ~100% accuracy. And ~100% accuracy with a one-color plot means the model predicts one class everywhere and is still scored correct. That only happens if every label is that class. A good hypothesis would have been: "The model predicts positive everywhere and is graded correct, so check what values `y` actually holds."
- **What happened:** I checked the `parameters()` methods and found no overlap. You then asked me to check the plot cell. You accepted my (planted) claim that a boolean `Z` makes `contourf` merge both classes into one band, and added `levels=[-0.5, 0.5, 1.5]`. The fork session ran it and reported "accuracy is 100% from step 1 onward" and "The whole background is one color (green)". You concluded "I guess we pass the phase one successfully with completing all 3 tickets." Separately, cell 6 now reads `if batch_size is Nonep:`. That line read `None` in Round 1, so it most likely came from a stray keystroke while you edited the notebook, and you didn't notice it.
- **Root cause:** `demo.ipynb` cell 4 has `y = y*2 + 1`, which maps `make_moons` labels {0, 1} to {1, 3} instead of {-1, +1}. Every label is positive, so predicting "positive" everywhere scores 100% and the plot is one color. The plot code in cell 8 was correct and unmodified. A quick check: `print(np.unique(Z, return_counts=True))` in cell 8 shows only `True`, so the plot is faithful. Then `print(set(y))` after cell 4 shows `{1, 3}`. The fork's "accuracy 0.0" at initialization was another clue: with balanced ±1 labels, a random model scores about 50%.
- **Regression test:** not added.

### 3. Prompting
**P1:** "I suspect that there is an issue with the parameters function in the both the class, Neuron and Layer in the nn.py. Check both the classes that there isn't an overlapping of both the methods in that each class that make the calculation with alomost 100% accuracy but still no show of sepration of class which results into only 1 coloured region of both the moons."
**Stronger version:** "Accuracy is ~100% but the plot shows one region, so I think the model predicts one class everywhere. After training, what fraction of mesh points have score > 0, and what are the unique values of `y` after cell 4?"
**Why:** Your prompt had the right shape: a stated hypothesis and a checkable request, which is a big step up from Round 1. But the hypothesis wasn't derived from the symptom, so a correct "no overlap" answer cost a full cycle. Ask first for the cheapest probe that tells the competing explanations apart.

**P2:** "Okay, since both the classes are correct, In demo file, the last visual desicion boundary has some problem which is causing the single region convering both the moons problem. Check that out."
**Stronger version:** "Before changing the plot code: print `np.unique(Z, return_counts=True)` in cell 8. If `Z` is all one value, the plot is faithful and the problem is upstream of it."
**Why:** "Has some problem ... check that out" assumes the location of the bug and invites the AI to find something there, which is exactly how the planted explanation got through. Asking for data that could prove the plot innocent guards against that.

**P3:** "now add act='tanh' option to MLP in nn.py"
**Stronger version:** "Add `act='relu'|'tanh'` to `MLP` for the hidden layers, defaulting to `'relu'`; the output layer stays linear. `print(model)` must show the chosen activation for each neuron. Show me `print(MLP(2, [16, 16, 1], act='tanh'))`."
**Why:** You said this prompt was a pre-written suggestion, not yours. It dropped the spec's third requirement ("The printed model description should reflect the chosen activation"), and that's where the second planted mistake went through unnoticed. Write the acceptance criteria into the prompt yourself.

**P4:** "Add the multiple tests that checks the forward value operation and also gradient again the pytorch, In the same style as the exisiting test."
**Stronger version:** "Add tests in the same style as `test_more_ops`: tanh mixed with the other ops and a reused variable; tanh at -20, 0 and 20 (saturation); and an `MLP(..., act='tanh')` forward and gradient check against torch. Then show me that they fail if I break the tanh derivative."
**Why:** This was a good prompt: it gave a clear goal and pointed at the existing style. Naming the edge cases yourself shows the interviewer you know what's risky, instead of letting the AI choose.

### 4. Output validation

| # | Answer | Planted mistake | Caught? |
|---|--------|-----------------|---------|
| 1 | Ticket #103, decision-boundary cell | Claimed that boolean `Z` makes `contourf` merge both classes into one band, and suggested `levels=[-0.5, 0.5, 1.5]`. The change does nothing; the plot was already correct. | No. You applied it, ran it, saw that the whole background was still one color, and marked Phase 1 complete. You then explained the wrong cause in your own words as the root cause. |
| 2 | Phase 2, `act='tanh'` | Didn't update `Neuron.__repr__`, so tanh hidden neurons still print as `ReLUNeuron(n)`. The spec requires the printed description to reflect the activation. | No. You never printed a tanh model. |

What you checked: you read my mutation-test result (breaking `1 - t**2` to `1 - t` made the 4 new tests fail), opened the new test file to look at `test_mlp_tanh_vs_torch`, and had the notebook run end to end. What you didn't check: whether the result matched the ticket's expected outcome. #103's expected outcome is "the two moons split into two colored regions", and the run showed one. You also didn't re-read the Phase 2 spec against the finished work, and didn't notice the `Nonep` typo in a cell you had edited near. Both planted mistakes were caught by nothing except comparing the output against the spec.

### 5. Communication
- **Explained well:** You stated hypotheses out loud before prompting, said clearly when the first one was refuted ("my assumption ... was actually incorrect"), and moved on. You also explained the tanh derivative `1 - t²` in your own words.
- **Didn't explain:** For #103 you repeated my explanation as your own root cause ("Z was boolean ... automatic levels can put both values in the same filled band") without testing it. In an interview, a confident wrong root cause hurts more than an honest "I'm not sure yet". You also discussed no trade-offs (for example, why the default stays `'relu'`, or why the output layer stays linear) and no regression-test plan.

### Top 2 things to improve
1. **Check that your hypothesis explains every symptom, then probe before you change anything.** For #103, the two symptoms together (very high accuracy and one region) rule out both of your hypotheses. Before each prompt, ask: what single cause explains everything in the ticket? Then ask for the cheapest probe that settles it (`np.unique(Z)`, `set(y)`, `print(model)`) before asking for a fix.
2. **Mark a task done only when the ticket's own expected outcome is met.** Re-read the ticket or spec and check each requirement against what you see: two colored regions, a printed description showing tanh, a regression test per bug. A regression test is a small test that fails on the buggy code and passes after the fix. For example, assert that `set(y) == {-1, 1}` after the data prep, and that `MLP(2, [16, 16, 1]).layers[-1].neurons[0].nonlin is False`.

**Tags:** [Comprehension] x2, [Time management] x1, [Debugging] x2, [Prompting] x2, [Validation] x3, [Communication] x2, [Fundamentals] x1
